package org.debilla.github_repo_details.service;

import org.debilla.github_repo_details.dto.GithubRepoDTO;
import org.debilla.github_repo_details.exceptions.RepositoryNotFoundException;
import org.debilla.github_repo_details.model.GithubRepoDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@EnableCaching
@Service
public class GithubRepoDetailsService {

    private final RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    public GithubRepoDetailsService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Cacheable(value = "repoDetailsCache", key = "#owner + '/' + #repositoryName")
    public GithubRepoDTO getRepositoryDetails(String owner, String repositoryName) {
        String url = String.format("%s/repos/%s/%s", this.githubApiUrl, owner, repositoryName);
        try {
            ResponseEntity<GithubRepoDetailsResponse> response = this.restTemplate.getForEntity(url, GithubRepoDetailsResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                GithubRepoDTO githubRepoDTO = new GithubRepoDTO();
                githubRepoDTO.setFullName(response.getBody().getFullName());
                githubRepoDTO.setDescription(response.getBody().getDescription());
                githubRepoDTO.setStars(response.getBody().getStars());
                githubRepoDTO.setCloneUrl(response.getBody().getCloneUrl());
                githubRepoDTO.setCreatedAt(response.getBody().getCreatedAt());
                return githubRepoDTO;
            } else {
                throw new RepositoryNotFoundException("Failed to fetch repository details: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new RepositoryNotFoundException("HTTP error while fetching repository details: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching repository details: " + e.getMessage(), e);
        }
    }
}
