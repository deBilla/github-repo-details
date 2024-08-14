package org.debilla.github_repo_details.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.debilla.github_repo_details.model.GithubRepoDetailsResponse;
import org.debilla.github_repo_details.service.GithubRepoDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositories")
public class GithubRepoDetailsController {
    private final GithubRepoDetailsService githubRepoDetailsService;

    public GithubRepoDetailsController(GithubRepoDetailsService githubRepoDetailsService) {
        this.githubRepoDetailsService = githubRepoDetailsService;
    }
    @Operation(summary = "Get item by ID", description = "Retrieve an item by its ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval")
    @ApiResponse(responseCode = "404", description = "Repository not found")
    @GetMapping("/{owner}/{repositoryName}")
    public ResponseEntity<GithubRepoDetailsResponse> getRepositoryDetails(
            @Parameter(description = "GitHub repository owner ID") @PathVariable String owner,
            @Parameter(description = "Github repository name") @PathVariable String repositoryName) {
        GithubRepoDetailsResponse response = githubRepoDetailsService.getRepositoryDetails(owner, repositoryName);
        return ResponseEntity.ok(response);
    }
}
