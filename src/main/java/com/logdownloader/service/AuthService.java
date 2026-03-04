package com.logdownloader.service;

import com.logdownloader.dto.LoginRequest;
import com.logdownloader.dto.LoginResponse;
import com.logdownloader.model.User;
import com.logdownloader.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return new LoginResponse(user.getUsername(), user.getRole());
    }

}