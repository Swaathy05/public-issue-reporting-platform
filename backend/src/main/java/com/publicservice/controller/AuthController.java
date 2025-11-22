package com.publicservice.controller;

import com.publicservice.dto.LoginRequest;
import com.publicservice.dto.UserRequest;
import com.publicservice.entity.User;
import com.publicservice.security.JwtUtils;
import com.publicservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Manual authentication to avoid framework 400/403 noise during setup
            User user = userService.findByUsername(loginRequest.getUsername()).orElse(null);
            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                Map<String, String> err = new HashMap<>();
                err.put("error", "Invalid credentials");
                return ResponseEntity.status(401).body(err);
            }

            // Also set security context for downstream filters if any expect it
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateToken(user);
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");
            response.put("user", createUserResponse(user));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Login failed: " + e.getMessage());
            return ResponseEntity.status(500).body(err);
        }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            User user = userService.createUser(userRequest);
            String jwt = jwtUtils.generateToken(user);
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");
            response.put("user", createUserResponse(user));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/admin/signup")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserRequest userRequest) {
        try {
            User user = userService.createAdmin(userRequest);
            String jwt = jwtUtils.generateToken(user);
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");
            response.put("user", createUserResponse(user));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("username", user.getUsername());
        userResponse.put("email", user.getEmail());
        userResponse.put("fullName", user.getFullName());
        userResponse.put("role", user.getRole());
        return userResponse;
    }
}
