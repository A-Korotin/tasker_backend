package com.korotin.tasker.controller;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.UserDTO;
import com.korotin.tasker.exception.NotFoundException;
import com.korotin.tasker.mapper.UserMapper;
import com.korotin.tasker.service.UserService;
import com.korotin.tasker.validator.annotation.ExistingId;
import com.korotin.tasker.validator.annotation.UniqueUserEmail;
import com.korotin.tasker.validator.annotation.ValidUserDTO;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final UserMapper userMapper;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public OutputUserDTO getCurrentAuthorizedUser(Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElseThrow(() ->
                new NotFoundException("User with login '%s' could not be found".formatted(principal.getName())));

        return userMapper.userToDTO(user);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') || #userId == authentication.principal.id")
    public OutputUserDTO getUserById(@PathVariable @ExistingId(responsible = UserService.class) UUID userId) {
        User user = userService.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id '%s' could not be found".formatted(userId)));

        return userMapper.userToDTO(user);
    }

    @Parameter(name = "response", hidden = true)
    @PostMapping
    @Secured("ROLE_ADMIN")
    public OutputUserDTO createUser(@Valid @RequestBody @UniqueUserEmail UserDTO userDTO, HttpServletResponse response) {
        User user = userService.save(userMapper.DTOToUser(userDTO));
        response.setStatus(HttpServletResponse.SC_CREATED);
        return userMapper.userToDTO(user);
    }

    @PutMapping("/{userId}")
    @ValidUserDTO(dtoIndex = 0, idIndex = 1)
    @PreAuthorize("hasRole('ADMIN') || #userId == authentication.principal.id")
    public OutputUserDTO editUser(@Valid @RequestBody UserDTO userDTO,
                                  @PathVariable @ExistingId(responsible = UserService.class) UUID userId) {
        User updated = userService.update(userId, userMapper.DTOToUser(userDTO));
        return userMapper.userToDTO(updated);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public void deleteUser(@PathVariable @ExistingId(responsible = UserService.class) UUID userId) {
        userService.delete(userId);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public List<OutputUserDTO> getAllUsers() {
        return StreamSupport.stream(userService.findAll().spliterator(), false)
                .map(userMapper::userToDTO)
                .toList();
    }
}
