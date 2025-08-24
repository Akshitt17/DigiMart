package com.smarthome.user.service;

import com.smarthome.user.dto.LoginRequest;
import com.smarthome.user.dto.LoginResponse;
import com.smarthome.user.dto.RegisterRequest;
import com.smarthome.user.entity.UserEntity;
import com.smarthome.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getStatus()) {
            throw new RuntimeException("User account is inactive");
        }

        // Include userId in JWT token
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name(), user.getUserId());

        return new LoginResponse(
                token,
                user.getUserId(),
                user.getUsername(),
                user.getRole().name(),
                "Login successful"
        );
    }

    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserEntity.Role.valueOf(request.getRole().toUpperCase()));
        user.setStatus(true);

        userRepository.save(user);
        return "User registered successfully";
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserEntity user = userRepository.findByUsername(username).orElse(null);
            return user != null && jwtService.isTokenValid(token, username);
        } catch (Exception e) {
            return false;
        }
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
