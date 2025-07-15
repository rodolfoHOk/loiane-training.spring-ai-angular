package br.com.hioktec.api_ai.application.dto;

public record NewChatResponse(
        String chatId,
        String description,
        String response
) {
}
