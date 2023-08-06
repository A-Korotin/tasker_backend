package com.korotin.tasker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.korotin.tasker.domain.dto.*;
import com.korotin.tasker.source.UserTaskTestProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTaskAPITest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    @Qualifier("adminHttpBasic")
    private RequestPostProcessor adminCredentials;

    private UUID projectId;

    private UUID userId;
    private UUID adminId;

    private final String userEmail = "user@user.com";
    private final String userPassword = "password123";

    private UUID createProject(String name, UUID ownerId) throws Exception {
        ProjectDTO projectDTO = new ProjectDTO(name, ownerId);

        String response = mvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(projectDTO))
                        .with(adminCredentials))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();


        OutputProjectDTO project = fromJson(response, OutputProjectDTO.class);

        return project.id;
    }

    @BeforeAll
    @DisplayName("Register user, create default project")
    public void setUp() throws Exception {
        assertNotNull(mvc, "Mock Mvc should not be null");

        RegisterUserDto dto = new RegisterUserDto();
        dto.setName("User");
        dto.setEmail(userEmail);
        dto.setPassword(userPassword);

        String response = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO user = fromJson(response, OutputUserDTO.class);
        this.userId = user.id;
        this.projectId = createProject("Project", user.id);

        response = mvc.perform(get("/api/users/me")
                .with(adminCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO admin = fromJson(response, OutputUserDTO.class);

        this.adminId = admin.id;
    }

    @AfterAll
    public void cleanup() throws Exception {
        String response = mvc.perform(get("/api/tasks")
                .with(adminCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<OutputTaskDTO> tasks = fromJson(response, new TypeReference<>() {});

        for (var task: tasks) {
            mvc.perform(delete("/api/tasks/%s".formatted(task.id))
                    .with(adminCredentials))
                    .andExpect(status().isOk());
        }

        response = mvc.perform(get("/api/projects")
                .with(adminCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<OutputProjectDTO> projects = fromJson(response, new TypeReference<>() {});

        for (var project: projects) {
            mvc.perform(delete("/api/projects/%s".formatted(project.id))
                    .with(adminCredentials))
                    .andExpect(status().isOk());
        }

        response = mvc.perform(get("/api/users")
                .with(adminCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<OutputUserDTO> users = fromJson(response, new TypeReference<>() {});

        for (var user: users) {
            if (user.getEmail().equals("test@test.com")) continue;
            mvc.perform(delete("/api/users/%s".formatted(user.id))
                    .with(adminCredentials))
                    .andExpect(status().isOk());
        }
    }

    @Order(1)
    @ParameterizedTest
    @ArgumentsSource(UserTaskTestProvider.class)
    @DisplayName("Create task, then check if it exists")
    public void createTask_thenCheckIfExists(String name, String description, ZonedDateTime startDate,
                                             Boolean done, int status) throws Exception {
        UserTaskDTO dto = new UserTaskDTO();
        dto.setName(name);
        dto.setDescription(description);
        dto.setStartDate(startDate);
        dto.setDone(done);

        String response = mvc.perform(post("/api/projects/%s/tasks".formatted(projectId.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().is(status))
                .andReturn().getResponse().getContentAsString();

        if (status != 201) {
            return;
        }

        OutputTaskDTO taskDto = fromJson(response, OutputTaskDTO.class);


        response = mvc.perform(get("/api/projects/%s/tasks/%s".formatted(projectId, taskDto.id))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        OutputTaskDTO actualTaskDTO = fromJson(response, OutputTaskDTO.class);

        assertEquals(taskDto.id, actualTaskDTO.id);
        assertEquals(taskDto.name, actualTaskDTO.name);
        assertEquals(taskDto.description, actualTaskDTO.description);
        assertEquals(taskDto.done, actualTaskDTO.done);
        assertEquals(taskDto.projectId, actualTaskDTO.projectId);
    }

    @Test
    @Order(2)
    @DisplayName("Try to access non-existing task in project")
    public void accessNonExistingTask() throws Exception {
        mvc.perform(get("/api/projects/%s/tasks/%s".formatted(projectId, UUID.randomUUID()))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isNotFound());

        mvc.perform(delete("/api/projects/%s/tasks/%s".formatted(projectId, UUID.randomUUID()))
                        .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isNotFound());

        UserTaskDTO taskDTO = new UserTaskDTO();
        taskDTO.setName("Name");
        taskDTO.setDescription("");
        taskDTO.setDone(false);
        taskDTO.setStartDate(ZonedDateTime.now());

        mvc.perform(put("/api/projects/%s/tasks/%s".formatted(projectId, UUID.randomUUID()))
                .with(httpBasic(userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    @DisplayName("Try to access task that does not belong to the project")
    public void accessForeignTask() throws Exception {
        UUID newProjectId = createProject("Admin project", adminId);

        UserTaskDTO taskDTO = new UserTaskDTO();
        taskDTO.setName("Name");
        taskDTO.setDescription("");
        taskDTO.setDone(false);
        taskDTO.setStartDate(ZonedDateTime.now());

        String response = mvc.perform(post("/api/projects/%s/tasks".formatted(newProjectId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskDTO))
                .with(adminCredentials))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputTaskDTO task = fromJson(response, OutputTaskDTO.class);

        mvc.perform(get("/api/projects/%s/tasks/%s".formatted(projectId, task.id))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isNotFound());

        mvc.perform(put("/api/projects/%s/tasks/%s".formatted(projectId, task.id))
                .with(httpBasic(userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskDTO)))
                .andExpect(status().isNotFound());

        mvc.perform(delete("/api/projects/%s/tasks/%s".formatted(projectId, task.id))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @Order(4)
    @ArgumentsSource(UserTaskTestProvider.class)
    @DisplayName("Try edit task, then check if it changed")
    public void tryEditTask_thenCheckIfChanged(String name, String description, ZonedDateTime startDate,
                                                  Boolean done, int status) throws Exception {
        UserTaskDTO taskDTO = new UserTaskDTO();
        taskDTO.setName("Name");
        taskDTO.setDescription("");
        taskDTO.setDone(false);
        taskDTO.setStartDate(ZonedDateTime.now());

        String response = mvc.perform(post("/api/projects/%s/tasks".formatted(projectId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(taskDTO))
                        .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputTaskDTO task = fromJson(response, OutputTaskDTO.class);


        UserTaskDTO editDTO = new UserTaskDTO();
        editDTO.setName(name);
        editDTO.setDescription(description);
        editDTO.setDone(done);
        editDTO.setStartDate(startDate);


        response = mvc.perform(put("/api/projects/%s/tasks/%s".formatted(projectId, task.id))
                .with(httpBasic(userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(editDTO)))
                .andExpect(status().is(status == 201 ? --status : status))
                .andReturn().getResponse().getContentAsString();

        if (status != 200) {
            return;
        }

        OutputTaskDTO actual = fromJson(response, OutputTaskDTO.class);

        assertEquals(name, actual.name);
        assertEquals(description, actual.description);
        assertTrue(startDate.isEqual(actual.startDate));
        assertEquals(done, actual.done);


        response = mvc.perform(get("/api/projects/%s/tasks/%s".formatted(projectId, actual.id))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        OutputTaskDTO returned = fromJson(response, OutputTaskDTO.class);

        assertEquals(name, returned.name);
        assertEquals(description, returned.description);
        assertTrue(startDate.isEqual(actual.startDate));
        assertEquals(done, returned.done);
    }

    @Test
    @Order(5)
    @DisplayName("Delete task, then check if it changed")
    public void deleteTask_thenCheckIfExists() throws Exception {
        UserTaskDTO taskDTO = new UserTaskDTO();
        taskDTO.setName("Name");
        taskDTO.setDescription("");
        taskDTO.setDone(false);
        taskDTO.setStartDate(ZonedDateTime.now());

        String response = mvc.perform(post("/api/projects/%s/tasks".formatted(projectId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(taskDTO))
                        .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputTaskDTO task = fromJson(response, OutputTaskDTO.class);

        mvc.perform(delete("/api/projects/%s/tasks/%s".formatted(projectId, task.id))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isOk());

        mvc.perform(get("/api/projects/%s/tasks/%s".formatted(projectId, task.id))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    @DisplayName("Try access foreign project")
    public void tryAccessForeignProject() throws Exception {
        UUID adminProjectId = createProject("Admin project", adminId);

        mvc.perform(get("/api/projects/%s/tasks".formatted(adminProjectId))
                .with(httpBasic(userEmail, userPassword)))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/projects/%s/tasks".formatted(adminProjectId))
                .with(adminCredentials))
                .andExpect(status().isOk());
    }
}
