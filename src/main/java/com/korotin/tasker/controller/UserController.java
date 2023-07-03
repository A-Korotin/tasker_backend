package com.korotin.tasker.controller;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.UserDTO;
import com.korotin.tasker.exception.NotFoundException;
import com.korotin.tasker.mapper.UserMapper;
import com.korotin.tasker.service.UserService;
import com.korotin.tasker.validator.annotation.UniqueUserEmail;
import com.korotin.tasker.validator.annotation.ValidUserEmail;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @GetMapping("/me")
    public OutputUserDTO getCurrentAuthorizedUser(Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElseThrow(() ->
                new NotFoundException("User with login '%s' could not be found".formatted(principal.getName())));

        return userMapper.userToDTO(user);
    }

    @GetMapping("/{userId}")
    public OutputUserDTO getUserById(@PathVariable UUID userId) {
        User user = userService.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id '%s' could not be found".formatted(userId)));

        return userMapper.userToDTO(user);
    }

    @Parameter(name = "response", hidden = true)
    @PostMapping
    public OutputUserDTO createUser(@Valid @RequestBody @UniqueUserEmail UserDTO userDTO, HttpServletResponse response) {
        User user = userService.save(userMapper.DTOToUser(userDTO));
        response.setStatus(HttpServletResponse.SC_CREATED);
        return userMapper.userToDTO(user);
    }

    @PutMapping("/{userId}")
    @ValidUserEmail(dtoIndex = 0, idIndex = 1)
    public OutputUserDTO editUser(@Valid @RequestBody UserDTO userDTO, @PathVariable UUID userId) {
        User updated = userService.update(userId, userMapper.DTOToUser(userDTO));
        return userMapper.userToDTO(updated);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
    }

    @GetMapping
    public List<OutputUserDTO> getAllUsers() {
        return StreamSupport.stream(userService.findAll().spliterator(), false)
                .map(userMapper::userToDTO)
                .toList();
    }
}
