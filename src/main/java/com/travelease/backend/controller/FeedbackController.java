package com.travelease.backend.controller;

import com.travelease.backend.model.Feedback;
import com.travelease.backend.repository.FeedbackRepository;
import com.travelease.backend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.travelease.backend.dto.FeedbackRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;
    private final EmailService emailService;

    public FeedbackController(FeedbackRepository feedbackRepository, EmailService emailService) {
        this.feedbackRepository = feedbackRepository;
        this.emailService = emailService;
    }

    @PostMapping("/api/feedback")
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackRequest body) {
        String name = body.getName();
        String email = body.getEmail();
        String message = body.getDescription() != null ? body.getDescription() : "";
        String subject = body.getSubject() != null ? body.getSubject() : "";

        if (name == null || email == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing fields");
        }

        Feedback fb = new Feedback(name, email, subject, message);
        feedbackRepository.save(fb);

        String emailSubject = "Thanks for your feedback — TravelEase";
        String html = buildHtmlResponse(name, subject, message);

        try {
            emailService.sendHtmlMessage(email, emailSubject, html);
        } catch (Exception e) {
            System.err.println("Email Send Error: " + e.getMessage());
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "ok");
        resp.put("id", fb.getId());
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/api/admin/feedback")
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    private String buildHtmlResponse(String name, String subject, String message) {
        String safeName = escapeHtml(name);
        String safeSubject = escapeHtml(subject.isEmpty() ? "No subject" : subject);
        String safeMessage = escapeHtml(message.isEmpty() ? "No message" : message);

        return String.format("""
<table width="100%%" cellpadding="0" cellspacing="0" style="background:#0f172a;padding:25px 0;">
<tr>
<td align="center">

<table width="550" cellpadding="0" cellspacing="0" 
style="background:#ffffff;border-radius:12px;overflow:hidden;font-family:Arial,Helvetica,sans-serif;">

<tr>
<td style="
    padding:40px 25px;
    text-align:center;
    color:#ffffff;
    background: linear-gradient(135deg, #4392A5 0%%, #D1803B 100%%);
">
<div style="font-size:40px;">🚗</div>
<div style="font-size:30px;font-weight:700;">TravelEase</div>
<div style="font-size:11px;letter-spacing:2px;margin-top:6px;">
YOUR JOURNEY BEGINS HERE
</div>
</td>
</tr>

<tr>
<td style="padding:25px;text-align:left;color:#1e293b;">
<div style="font-size:22px;font-weight:700;margin-bottom:10px;">
Thank You, <span style="color:#0891b2;">%s</span>! 🎉
</div>

<div style="font-size:15px;line-height:1.6;color:#475569;margin-bottom:20px;">
We greatly appreciate your feedback. Your message helps us improve our travel services.
</div>

<table cellpadding="0" cellspacing="0" width="100%%" 
style="background:#f0f9ff;border-left:3px solid #0891b2;padding:15px;border-radius:8px;">
<tr><td style="font-size:14px;color:#334155;font-weight:700;">📧 Your Feedback</td></tr>

<tr><td style="padding-top:10px;font-size:14px;color:#334155;font-weight:700;">Subject:</td></tr>
<tr><td style="font-size:14px;color:#475569;padding-top:5px;">%s</td></tr>

<tr><td style="padding-top:15px;font-size:14px;color:#334155;font-weight:700;">Message:</td></tr>
<tr><td style="font-size:14px;color:#475569;padding-top:5px;white-space:pre-wrap;">%s</td></tr>
</table>

<div style="height:1px;background:#e2e8f0;margin:25px 0;"></div>

<table cellpadding="0" cellspacing="0" width="100%%" 
style="background:#fef3c7;border-left:3px solid #f97316;padding:15px;border-radius:8px;">
<tr><td style="font-size:14px;color:#663c00;font-weight:700;">🚀 What's Next?</td></tr>
<tr>
<td style="padding-top:8px;font-size:14px;color:#7c5500;line-height:1.6;">
Our team will review your message and respond within 24–48 hours if required.
</td>
</tr>
</table>

<div style="font-size:15px;color:#475569;margin-top:18px;line-height:1.6;">
🏝️ Explore our latest travel packages and exclusive deals. Your next trip awaits!
</div>

</td>
</tr>

<tr>
<td style="padding:20px;text-align:center;background:#f8fafc;border-top:1px solid #e2e8f0;">
<div style="font-size:14px;color:#64748b;">Warm regards,</div>
<div style="font-size:16px;font-weight:700;color:#0891b2;margin-top:5px;">The TravelEase Team</div>

<div style="margin-top:12px;font-size:13px;color:#64748b;line-height:1.6;">
✉️ <a href="mailto:support@travelease.com" style="color:#0891b2;text-decoration:none;font-weight:600;">
support@travelease.com</a><br>
📞 <a href="tel:+917995762616" style="color:#0891b2;text-decoration:none;font-weight:600;">
+91 7995762616</a>
</div>
</td>
</tr>

</table>

</td>
</tr>
</table>
""", safeName, safeSubject, safeMessage);
    }

    private String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
