package com.korotin.tasker.source;

import com.korotin.tasker.domain.UserRole;
import com.korotin.tasker.domain.dto.UserDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UpdateUserTestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        UserDTO init1 = UserDTO.builder()
                .name("1").email("123@123.com").password("12345").role(UserRole.USER).build();
        UserDTO update1 = UserDTO.builder()
                .name("2").email("1234@1234.com").password("12345").role(UserRole.USER).build();

        UserDTO init2 = UserDTO.builder()
                .name("1").email("qwerty@qwerty.com").password("123456").role(UserRole.USER).build();
        UserDTO update2 = UserDTO.builder()
                .name("2").email("qwerty@qwerty.com").password("123456789").role(UserRole.USER).build();

        UserDTO init3 = UserDTO.builder()
                .name("1").email("123@123.com").password("12345").role(UserRole.USER).build();
        UserDTO update3 = UserDTO.builder()
                .name("Does not matter").email("test@test.com").password("123456").role(UserRole.USER).build();

        return Stream.of(
                Arguments.of(init1, update1, 200),
                Arguments.of(init2, update2, 200),
                Arguments.of(init3, update3, 409)
        );
    }
}
