package br.com.hioktec.api_ai.application.service;

import br.com.hioktec.api_ai.application.dto.ErrorResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.stereotype.Service;

@Service
public class MemoryChatService {

    private final ChatClient chatClient;

    public MemoryChatService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String memoryChat(String message) {
        try {
            return this.chatClient.prompt()
                    .user(message)
                    .call()
                    .content();
        } catch (Exception ex) {
            if (ex instanceof NonTransientAiException) {
                return new ErrorResponse(429, "Too Many Requests", "insufficient quota")
                        .toString();
            } else {
                throw ex;
            }
        }
    }
}
