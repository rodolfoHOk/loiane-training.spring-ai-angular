package br.com.hioktec.api_ai.application.dto;

public record ErrorResponse(
        int status,
        String message,
        String cause
) {

    @Override
    public String toString() {
        return "{" +
                "status: " + status +
                ", message: " + '\"' + message + '\"' +
                ", cause: " + '\"' + cause + '\"' +
                '}';
    }
}
