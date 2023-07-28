package com.korotin.tasker.controller;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.RegisterUserDto;
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
    private final UserMapper mapper;

    @Parameter(name = "response", hidden = true)
    @PostMapping("/register")
    public OutputUserDTO register(@Valid @RequestBody RegisterUserDto dto, HttpServletResponse response) {
        User user = userService.save(mapper.registerDTOToUser(dto));
        response.setStatus(HttpServletResponse.SC_CREATED);

        return mapper.userToDTO(user);
    }

}
