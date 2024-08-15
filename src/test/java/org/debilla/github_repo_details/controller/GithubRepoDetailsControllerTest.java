package org.debilla.github_repo_details.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.debilla.github_repo_details.dto.GithubRepoDTO;
import org.debilla.github_repo_details.exceptions.RepositoryNotFoundException;
import org.debilla.github_repo_details.service.GithubRepoDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class GithubRepoDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GithubRepoDetailsService githubRepoDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    public GithubRepoDetailsControllerTest() {
    }

    @Test
    void testGetRepositoryDetails_Success() throws Exception {
        // Given
        String owner = "owner";
        String repositoryName = "repo";
        GithubRepoDTO mockResponse = new GithubRepoDTO();
        mockResponse.setFullName("Test Repo");
        mockResponse.setDescription("A test repository");
        when(githubRepoDetailsService.getRepositoryDetails(owner, repositoryName))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{owner}/{repositoryName}", owner, repositoryName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(mockResponse.getFullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(mockResponse.getDescription()))
                .andDo(print());
    }

    @Test
    void testGetRepositoryDetails_NotFound() throws Exception {
        // Given
        String owner = "owner";
        String repositoryName = "repo";
        when(githubRepoDetailsService.getRepositoryDetails(owner, repositoryName))
                .thenThrow(new RepositoryNotFoundException("Repository not found"));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{owner}/{repositoryName}", owner, repositoryName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void testGetRepositoryDetails_InternalServerError() throws Exception {
        // Given
        String owner = "owner";
        String repositoryName = "repo";
        when(githubRepoDetailsService.getRepositoryDetails(owner, repositoryName))
                .thenThrow(new RuntimeException("Internal server error"));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{owner}/{repositoryName}", owner, repositoryName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
