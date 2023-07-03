package com.korotin.tasker;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin@admin.com", password = "123456", roles = "ADMIN")
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
}
