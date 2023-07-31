package com.korotin.tasker.source;

import com.korotin.tasker.domain.UserRole;
import com.korotin.tasker.domain.dto.UserDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class CreateUserTestProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        UserDTO validUser = UserDTO.builder()
                .name("Valid name")
                .email("valid@email.dom")
                .password("validPassword")
                .role(UserRole.USER)
                .build();
        UserDTO invalidEmail = UserDTO.builder()
                .name("Valid name")
                .email("invalid email")
                .password("validPassword")
                .role(UserRole.USER)
                .build();
        UserDTO invalidPassword = UserDTO.builder()
                .name("Valid name")
                .email("valid@email.dom")
                .password("shrt") // too short
                .role(UserRole.USER)
                .build();
        UserDTO invalid = UserDTO.builder()
                .name("") // invalid blank name
                .email("") // invalid blank email
                .password("") // invalid blank password
                .role(UserRole.USER)
                .build();
        UserDTO conflict = UserDTO.builder()
                .name("Valid name")
                .email("test@test.com")
                .password("1234567")
                .role(UserRole.USER)
                .build();
        return Stream.of(
                Arguments.of(validUser, 201),
                Arguments.of(invalidEmail, 400),
                Arguments.of(invalidPassword, 400),
                Arguments.of(invalid, 400),
                Arguments.of(conflict, 409)
        );
    }
}
