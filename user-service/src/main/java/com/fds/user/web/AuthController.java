package com.fds.user.web;

import com.fds.user.domain.UserAccount;
import com.fds.user.dto.AuthDtos;
import com.fds.user.repo.UserRepository;
import com.fds.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {
        String token = authService.register(req.email, req.password, req.role);
        UserAccount user = userRepository.findByEmail(req.email).orElseThrow();
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, user.getId(), user.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        String token = authService.login(req.email, req.password);
        UserAccount user = userRepository.findByEmail(req.email).orElseThrow();
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, user.getId(), user.getRole().name()));
    }
}

