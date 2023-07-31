package com.korotin.tasker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.korotin.tasker.util.JsonUtils;
import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.UserRole;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.RegisterUserDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.korotin.tasker.util.JsonUtils.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @Qualifier("adminHttpBasic")
    private RequestPostProcessor adminCredentials;

    @Test
    void contextLoads() {
        Assert.notNull(mvc, "Mock MVC should not be null");
        Assert.notNull(adminCredentials, "Admin credentials should not be null");
    }

    @BeforeEach
    @AfterAll
    public void clearUsers() throws Exception {
        String usersJson = mvc.perform(get("/api/users")
                        .with(adminCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<OutputUserDTO> users = fromJson(usersJson, new TypeReference<>(){});

        for (var user: users) {
            if (user.getEmail().equals("test@test.com")) {
                continue;
            }

            mvc.perform(delete("/api/users/" + user.getId())
                            .with(adminCredentials))
                    .andExpect(status().isOk());
        }
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
