package com.githubviewer.githubrepoviewer.dto.github;

public class GitHubBranch {
    private String name;
    private GitHubCommit commit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GitHubCommit getCommit() {
        return commit;
    }

    public void setCommit(GitHubCommit commit) {
        this.commit = commit;
    }
}