package com.githubviewer.githubrepoviewer.service;

import com.githubviewer.githubrepoviewer.dto.BranchResponse;
import com.githubviewer.githubrepoviewer.dto.RepositoryResponse;
import com.githubviewer.githubrepoviewer.dto.github.GitHubBranch;
import com.githubviewer.githubrepoviewer.dto.github.GitHubRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRepositoryService {

    private final GitHubClient githubClient;

    public UserRepositoryService(GitHubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<RepositoryResponse> getUserNonForkRepositories(String username) {
        List<GitHubRepository> repositories = githubClient.getUserRepositories(username);

        return repositories.stream()
                .filter(repo -> !repo.isFork())
                .map(this::mapToRepositoryResponse)
                .collect(Collectors.toList());
    }

    private RepositoryResponse mapToRepositoryResponse(GitHubRepository gitHubRepository) {
        List<GitHubBranch> branches = githubClient.getRepositoryBranches(
                gitHubRepository.getOwner().getLogin(),
                gitHubRepository.getName()
        );

        List<BranchResponse> branchResponses = branches.stream()
                .map(branch -> new BranchResponse(branch.getName(), branch.getCommit().getSha()))
                .collect(Collectors.toList());

        return new RepositoryResponse(
                gitHubRepository.getName(),
                gitHubRepository.getOwner().getLogin(),
                branchResponses
        );
    }
}