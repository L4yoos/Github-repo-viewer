package com.githubviewer.githubrepoviewer.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitHubRepository {
    private String name;
    private GitHubOwner owner;
    private boolean fork;

    public GitHubRepository(String name, GitHubOwner owner, boolean fork) {
        this.name = name;
        this.owner = owner;
        this.fork = fork;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GitHubOwner getOwner() {
        return owner;
    }

    public void setOwner(GitHubOwner owner) {
        this.owner = owner;
    }

    @JsonProperty("fork")
    public boolean isFork() {
        return fork;
    }

    @JsonProperty("fork")
    public void setFork(boolean fork) {
        this.fork = fork;
    }
}