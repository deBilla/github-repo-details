package org.debilla.github_repo_details.service;

import org.debilla.github_repo_details.exceptions.RepositoryNotFoundException;
import org.debilla.github_repo_details.model.GithubRepoDetailsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GithubRepoDetailsServiceTest {
    @InjectMocks
    private GithubRepoDetailsService githubRepoDetailsService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        githubRepoDetailsService = new GithubRepoDetailsService(restTemplateBuilder);
    }


    @Test
    void testGetRepositoryDetails_Success() {
        // Given
        String owner = "owner";
        String repositoryName = "repo";
        GithubRepoDetailsResponse mockResponse = new GithubRepoDetailsResponse();
        ResponseEntity<GithubRepoDetailsResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(GithubRepoDetailsResponse.class)))
                .thenReturn(responseEntity);

        // When
        GithubRepoDetailsResponse result = githubRepoDetailsService.getRepositoryDetails(owner, repositoryName);

        // Then
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(GithubRepoDetailsResponse.class));
    }

    @Test
    void testGetRepositoryDetails_RestClientException() {
        // Given
        String owner = "owner";
        String repositoryName = "repo";
        when(restTemplate.getForEntity(anyString(), eq(GithubRepoDetailsResponse.class)))
                .thenThrow(new RestClientException("Service unavailable"));

        // When & Then
        RepositoryNotFoundException exception = assertThrows(RepositoryNotFoundException.class,
                () -> githubRepoDetailsService.getRepositoryDetails(owner, repositoryName));
        assertTrue(exception.getMessage().contains("HTTP error while fetching repository details"));
    }

    @Test
    void testGetRepositoryDetails_UnexpectedError() {
        // Given
        String owner = "owner";
        String repositoryName = "repo";
        when(restTemplate.getForEntity(anyString(), eq(GithubRepoDetailsResponse.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> githubRepoDetailsService.getRepositoryDetails(owner, repositoryName));
        assertTrue(exception.getMessage().contains("Unexpected error while fetching repository details"));
    }
}