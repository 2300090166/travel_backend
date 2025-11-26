package com.travelease.backend.service;

import com.travelease.backend.model.UserEntity;
import com.travelease.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.admin.email:admin@travelease.local}")
    private String adminEmail;

    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername(adminUsername)) {
            String hashed = passwordEncoder.encode(adminPassword);
            UserEntity admin = new UserEntity(adminUsername, hashed, adminEmail, "ADMIN");
            userRepository.save(admin);
            System.out.println("Default admin created");
        } else {
            // ensure role set and update password if different
            userRepository.findByUsername(adminUsername).ifPresent(u -> {
                boolean changed = false;
                if (!"ADMIN".equals(u.getRole())) {
                    u.setRole("ADMIN");
                    changed = true;
                }
                // if password does not match configured (we can't compare hashed to plain), overwrite
                u.setPassword(passwordEncoder.encode(adminPassword));
                changed = true;
                if (changed) userRepository.save(u);
            });
        }
    }
}
