package com.githubviewer.githubrepoviewer.service;

import com.githubviewer.githubrepoviewer.dto.github.GitHubBranch;
import com.githubviewer.githubrepoviewer.dto.github.GitHubRepository;
import com.githubviewer.githubrepoviewer.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GitHubClient {

    private final RestTemplate restTemplate;

    @Value("${github.api.base-url}")
    private String githubApiBaseUrl;

    //I had to add this PAT token to increase the API rate limit.
    @Value("${github.api.token:#{null}}")
    private String githubApiToken;

    public GitHubClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpEntity<String> createRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (githubApiToken != null && !githubApiToken.isEmpty()) {
            headers.set("Authorization", "Bearer " + githubApiToken);
        }
        headers.set("Accept", "application/vnd.github+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");
        return new HttpEntity<>(headers);
    }

    public List<GitHubRepository> getUserRepositories(String username) {
        String url = githubApiBaseUrl + "/users/" + username + "/repos";
        try {
            ResponseEntity<List<GitHubRepository>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createRequestEntity(),
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("GitHub user '" + username + "' not found.");
            }
            throw new RuntimeException("Error while fetching repositories for user: " + username + ". Status: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching repositories for user: " + username, e);
        }
    }

    public List<GitHubBranch> getRepositoryBranches(String owner, String repoName) {
        String url = githubApiBaseUrl + "/repos/" + owner + "/" + repoName + "/branches";
        try {
            ResponseEntity<List<GitHubBranch>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createRequestEntity(),
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error while fetching branches for repository: " + owner + "/" + repoName + ". Status: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching branches for repository: " + owner + "/" + repoName, e);
        }
    }
}