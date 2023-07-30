package com.korotin.tasker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.korotin.tasker.domain.dto.OutputProjectDTO;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.ProjectDTO;
import com.korotin.tasker.source.CreateProjectTestProvider;
import com.korotin.tasker.source.EditProjectTestProvider;
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

import java.util.List;
import java.util.UUID;

import static com.korotin.tasker.util.JsonUtils.asJsonString;
import static com.korotin.tasker.util.JsonUtils.fromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectCRUDTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @Qualifier("adminHttpBasic")
    private RequestPostProcessor adminCredentials;

    private UUID adminId;

    @BeforeAll
    public void setUp() throws Exception {
        assertNotNull(mvc, "Mock mvc should not be null");
        assertNotNull(adminCredentials, "Admin credentials should not be null");

        String adminJson = mvc.perform(get("/api/users/me")
                .with(adminCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        OutputUserDTO admin = fromJson(adminJson, OutputUserDTO.class);

        this.adminId = admin.getId();
    }

    @BeforeEach
    @AfterAll
    public void clearProjects() throws Exception {
        String projectsJson = mvc.perform(get("/api/projects")
                .with(adminCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<OutputProjectDTO> projects = fromJson(projectsJson, new TypeReference<>() {});

        for (var project:projects) {
            mvc.perform(delete("/api/projects/" + project.getId())
                    .with(adminCredentials))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void contextLoads() {
        assertNotNull(adminId, "Fetched admin ID should not be null");
    }

    @ParameterizedTest
    @ArgumentsSource(CreateProjectTestProvider.class)
    public void createProjectTest(String projectName, int status) throws Exception {
        ProjectDTO dto = ProjectDTO.builder()
                .name(projectName).ownerId(adminId).build();

        String projectJson = mvc.perform(post("/api/projects")
                        .with(adminCredentials)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().is(status))
                .andReturn().getResponse().getContentAsString();

        if (status != 200) {
            return;
        }

        OutputProjectDTO project = fromJson(projectJson, OutputProjectDTO.class);
        assertEquals(project.getName(), projectName);
        assertEquals(project.getOwner().getId(), adminId);
    }

    @ParameterizedTest
    @ArgumentsSource(EditProjectTestProvider.class)
    public void editProjectTest(String initName, String editName, int status) throws Exception{
        ProjectDTO init = ProjectDTO.builder()
                .name(initName).ownerId(adminId).build();
        String initJson = mvc.perform(post("/api/projects")
                .with(adminCredentials)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(init)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OutputProjectDTO initCreated = fromJson(initJson, OutputProjectDTO.class);
        assertEquals(initCreated.getName(), initName);
        assertEquals(initCreated.getOwner().getId(), adminId);

        ProjectDTO edit = ProjectDTO.builder()
                .name(editName).ownerId(adminId).build();

        String editedJson = mvc.perform(put("/api/projects/" + initCreated.getId())
                .with(adminCredentials)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(edit)))
                .andExpect(status().is(status))
                .andReturn().getResponse().getContentAsString();

        if (status != 200) {
            return;
        }

        OutputProjectDTO edited = fromJson(editedJson, OutputProjectDTO.class);

        assertEquals(edited.getId(), initCreated.getId());
        assertEquals(edited.getName(), editName);
        assertEquals(edited.getOwner().getId(), adminId);
    }
}
