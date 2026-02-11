package com.payApp.payrollapp.controller;

import com.payApp.payrollapp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController  {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username,
                                     @RequestParam String password) {

        if ("admin".equals(username) && "admin123".equals(password)) {

            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password("") // not needed here
                    .authorities("ROLE_ADMIN")
                    .build();

            String token = jwtUtil.generateToken(userDetails);

            return Map.of("token", token);
        }

        throw new RuntimeException("Invalid credentials");
    }


}
