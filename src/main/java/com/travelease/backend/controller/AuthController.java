package com.travelease.backend.controller;

import com.travelease.backend.model.UserEntity;
import com.travelease.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final com.travelease.backend.security.JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
                          com.travelease.backend.security.JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/auth/signin")
    public ResponseEntity<?> signIn(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing credentials");
        }

        java.util.Optional<UserEntity> found = userRepository.findByUsername(username);
        if (found.isPresent() && passwordEncoder.matches(password, found.get().getPassword())) {
            UserEntity u = found.get();
            Map<String, Object> res = new HashMap<>();
            res.put("username", u.getUsername());
            res.put("email", u.getEmail());
            res.put("role", u.getRole());
            // issue JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", u.getRole());
            String token = jwtUtil.generateToken(u.getUsername(), claims);
            res.put("token", token);
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");

        if (username == null || password == null || email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing fields");
        }

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        String hashed = passwordEncoder.encode(password);
        UserEntity u = new UserEntity(username, hashed, email, "USER");
        userRepository.save(u);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    // Admin endpoint to get all customers (non-admin users)
    @GetMapping("/api/admin/customers")
    public List<Map<String, Object>> getAllCustomers() {
        return userRepository.findAll().stream()
                .filter(user -> "USER".equals(user.getRole()))
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("username", user.getUsername());
                    userMap.put("email", user.getEmail());
                    userMap.put("role", user.getRole());
                    return userMap;
                })
                .collect(Collectors.toList());
    }
}
