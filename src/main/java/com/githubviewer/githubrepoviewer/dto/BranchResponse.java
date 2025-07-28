package com.githubviewer.githubrepoviewer.dto;

public record BranchResponse(
        String name,
        String lastCommitSha
) {}