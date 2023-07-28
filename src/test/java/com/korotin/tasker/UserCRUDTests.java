package com.korotin.tasker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.korotin.tasker.domain.UserRole;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.UserDTO;
import com.korotin.tasker.source.CreateUserTestProvider;
import com.korotin.tasker.source.UpdateUserTestProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserCRUDTests extends BaseTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void checkMvc() {
        Assert.notNull(mvc, "Mock mvc should not be null");
    }

    @BeforeEach
    @AfterAll
    public void clearUsers() throws Exception {
        String usersJson = mvc.perform(get("/api/users")
                        .with(httpBasic("test@test.com", "qwerty")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<OutputUserDTO> users = fromJson(usersJson, new TypeReference<>(){});

        for (var user: users) {
            if (user.getEmail().equals("test@test.com")) {
                continue;
            }

            mvc.perform(delete("/api/users/" + user.getId())
                            .with(httpBasic("test@test.com", "qwerty")))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(CreateUserTestProvider.class)
    public void createUser(UserDTO createDTO, int status) throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createDTO))
                        .with(httpBasic("test@test.com", "qwerty")))
                .andExpect(status().is(status));
    }

    @ParameterizedTest
    @ArgumentsSource(UpdateUserTestProvider.class)
    public void updateUser(UserDTO init, UserDTO update, int status) throws Exception{
        String userJSON = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(init))
                .with(httpBasic("test@test.com", "qwerty")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO user = fromJson(userJSON, OutputUserDTO.class);

        String updatedJSON = mvc.perform(put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(update))
                .with(httpBasic("test@test.com", "qwerty")))
                .andExpect(status().is(status))
                .andReturn().getResponse().getContentAsString();

        if (status != 200) {
            return;
        }

        OutputUserDTO updated = fromJson(updatedJSON, OutputUserDTO.class);

        assertEquals(user.getId(), updated.getId());
        assertEquals(updated.getEmail(), update.getEmail());
        assertEquals(updated.getName(), update.getName());
        assertEquals(updated.getRole(), update.getRole());
    }

    @Test
    public void getNotFoundTest() throws Exception {
        mvc.perform(get("/api/users/" + UUID.randomUUID())
                .with(httpBasic("test@test.com", "qwerty")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editNotFoundTest() throws Exception {
        UserDTO validUser = UserDTO.builder()
                .email("totally_valid@email.com").password("qwerty1").name("Valid name").role(UserRole.USER)
                .build();

        mvc.perform(put("/api/users/" + UUID.randomUUID())
                .with(httpBasic("test@test.com", "qwerty"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(validUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteNotFoundTest() throws Exception {
        mvc.perform(delete("/api/users/" + UUID.randomUUID())
                        .with(httpBasic("test@test.com", "qwerty")))
                .andExpect(status().isNotFound());
    }
}
