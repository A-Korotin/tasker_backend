package com.korotin.tasker.controller;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/user")
    public Principal tryAuth(Principal principal) {
        return principal;
    }

    @PostMapping("/register")
    public User register() {
        return null;
    }
}
