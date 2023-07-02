package com.korotin.tasker.config;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.UserRole;
import com.korotin.tasker.repository.UserRepository;
import com.korotin.tasker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig implements CommandLineRunner {

    private final UserService userService;

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("test@test.com").isPresent()) {
            return;
        }

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("qwerty");
        user.setName("Test user");
        user.setRole(UserRole.ADMIN);

        userService.save(user);
    }
}
