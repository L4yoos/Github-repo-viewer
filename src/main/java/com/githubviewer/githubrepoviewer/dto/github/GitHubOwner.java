package com.githubviewer.githubrepoviewer.dto.github;

public class GitHubOwner {
    private String login;

    public GitHubOwner(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}