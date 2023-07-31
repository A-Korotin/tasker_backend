package com.korotin.tasker;

import com.korotin.tasker.domain.UserRole;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.RegisterUserDto;
import com.korotin.tasker.domain.dto.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.UUID;
import java.util.stream.Stream;

import static com.korotin.tasker.util.JsonUtils.asJsonString;
import static com.korotin.tasker.util.JsonUtils.fromJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserSecurityTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @Qualifier("adminHttpBasic")
    private RequestPostProcessor adminCredentials;

    private UUID adminId;

    @BeforeAll
    @AfterEach
    public void loadContext() throws Exception {
        String response = mvc.perform(get("/api/users/me")
                .with(adminCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO admin = fromJson(response, OutputUserDTO.class);

        adminId = admin.getId();
    }



    @Test
    public void contextLoads() {
        assertNotNull(mvc, "Mock mvc should not be null");
        assertNotNull(adminCredentials, "Admin credentials should not be null");
        assertNotNull(adminId, "Fetched admin id should not be null");
    }

    private Stream<Arguments> testArguments() {
        return Stream.of(
                Arguments.of("Register", "register@register.com", "12345678")
        );
    }

    @ParameterizedTest
    @MethodSource(value = "testArguments")
    public void registerUser_thenCheckRestrictedAccess(String userName, String userEmail, String userPassword)
            throws Exception {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setName(userName);
        dto.setEmail(userEmail);
        dto.setPassword(userPassword);

        String response = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO user = fromJson(response, OutputUserDTO.class);

        assertAll(() -> assertEquals(userName, user.getName()),
                  () -> assertEquals(userEmail, user.getEmail()),
                  () -> assertEquals(UserRole.USER, user.getRole()));


        mvc.perform(get("/api/users/" + adminId)
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/users/" + user.getId())
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isOk());

        mvc.perform(get("/api/users")
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isForbidden());

        UserDTO editDto = UserDTO.builder()
                .name("userName")
                .email("edit@edit.com")
                .password(userPassword)
                .role(UserRole.USER).build();

        mvc.perform(put("/api/users/" + adminId)
                .with(httpBasic(userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(editDto)))
                .andExpect(status().isForbidden());

        mvc.perform(delete("/api/users/" + adminId)
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isForbidden());

        response = mvc.perform(put("/api/users/" + user.getId())
                .with(httpBasic(userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(editDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO editedUser = fromJson(response, OutputUserDTO.class);

        mvc.perform(delete("/api/users/" + user.getId())
                .with(httpBasic(editedUser.getEmail(), userPassword)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    public void testEditSelfNonAdminRole(String userName, String userEmail, String userPassword) throws Exception {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setName(userName);
        dto.setEmail(userEmail);
        dto.setPassword(userPassword);

        String response = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO user = fromJson(response, OutputUserDTO.class);
        assertEquals(UserRole.USER, user.getRole());

        UserDTO editDto = UserDTO.builder()
                .name(userName)
                .email(userEmail)
                .password(userPassword)
                .role(UserRole.ADMIN).build();

        mvc.perform(put("/api/users/" + user.getId())
                .with(httpBasic(userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest());

        mvc.perform(delete("/api/users/" + user.getId())
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isOk());
    }
}
