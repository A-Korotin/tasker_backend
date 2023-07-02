package com.korotin.tasker;

import com.korotin.tasker.domain.UserRole;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.RegisterUserDto;
import com.korotin.tasker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthTests extends BaseTests{

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        Assert.notNull(mvc, "Mock MVC should not be null");
    }

    @BeforeEach
    @AfterEach
    public void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void registerUser_thanCheckIfUserExists() throws Exception {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setName("Test user");
        dto.setEmail("testing@testing.com");
        dto.setPassword("qwerty");
        String response = mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO user = fromJson(response, OutputUserDTO.class);
        assertEquals(user.getRole(), UserRole.USER);
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getName(), dto.getName());


        response = mvc.perform(get("/api/users/" + user.getId())
                        .with(httpBasic(user.getEmail(), "qwerty")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO fetchedUser = fromJson(response, OutputUserDTO.class);
        assertEquals(user.getId(), fetchedUser.getId());
        assertEquals(user.getEmail(), fetchedUser.getEmail());
        assertEquals(user.getName(), fetchedUser.getName());
        assertEquals(user.getRole(), fetchedUser.getRole());
    }
}
