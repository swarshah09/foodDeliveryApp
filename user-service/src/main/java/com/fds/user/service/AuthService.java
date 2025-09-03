package com.fds.user.service;

import com.fds.user.domain.UserAccount;
import com.fds.user.domain.UserRole;
import com.fds.user.repo.UserRepository;
import com.fds.user.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public String register(String email, String password, String roleStr) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        UserAccount user = new UserAccount();
        user.setEmail(email.toLowerCase(Locale.ROOT));
        user.setPasswordHash(passwordEncoder.encode(password));
        if (roleStr != null && !roleStr.isBlank()) {
            user.setRole(UserRole.valueOf(roleStr.toUpperCase(Locale.ROOT)));
        }
        userRepository.save(user);
        return jwtService.createToken(user);
    }

    public String login(String email, String password) {
        UserAccount user = userRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtService.createToken(user);
    }
}

