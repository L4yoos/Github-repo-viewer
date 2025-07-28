package com.githubviewer.githubrepoviewer.exception;

public record ErrorResponse(
        int status,
        String message
) {}