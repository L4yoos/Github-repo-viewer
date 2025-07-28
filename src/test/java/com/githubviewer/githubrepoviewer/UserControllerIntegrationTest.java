package com.githubviewer.githubrepoviewer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return non-fork repositories and branches for an existing user (happy path)")
    void shouldReturnNonForkRepositoriesAndBranchesForExistingUser() throws Exception {
        //Given
        String existingUser = "L4yoos";

        //When
        mockMvc.perform(get("/api/repositories/{username}", existingUser)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").isNotEmpty())
                .andExpect(jsonPath("$[0].repositoryName").exists())
                .andExpect(jsonPath("$[0].ownerLogin").value(existingUser))
                .andExpect(jsonPath("$[0].branches").isArray())
                .andExpect(jsonPath("$[0].branches.length()").isNotEmpty())
                .andExpect(jsonPath("$[0].branches[0].name").exists())
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha").exists())

                .andExpect(jsonPath("$[0].fork").doesNotExist())
                .andExpect(jsonPath("$[1].fork").doesNotExist());
    }

    @Test
    @DisplayName("Should return 404 response for a non-existing user")
    void shouldReturn404ForNonExistingUser() throws Exception {
        //Given
        String nonExistingUser = "definitelynotarealgithubuserxyz123";

        //When
        mockMvc.perform(get("/api/repositories/{username}", nonExistingUser)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("GitHub user '" + nonExistingUser + "' not found."));
    }
}