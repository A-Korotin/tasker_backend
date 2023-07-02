package com.korotin.tasker.controller;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.RegisterUserDto;
import com.korotin.tasker.domain.dto.UserDTO;
import com.korotin.tasker.mapper.UserMapper;
import com.korotin.tasker.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/user")
    public Principal tryAuth(Principal principal) {
        return principal;
    }

    @Parameter(name = "response", hidden = true)
    @PostMapping("/register")
    public UserDTO register(@Valid @RequestBody RegisterUserDto dto, HttpServletResponse response) {
        User user = userService.register(UserMapper.INSTANCE.registerDTOToUser(dto));
        response.setStatus(HttpServletResponse.SC_CREATED);

        return UserMapper.INSTANCE.userToDTO(user);
    }

}
