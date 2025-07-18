package br.com.hioktec.api_ai.application.service;

import br.com.hioktec.api_ai.application.dto.Chat;
import br.com.hioktec.api_ai.application.dto.ChatMessage;
import br.com.hioktec.api_ai.application.dto.ErrorResponse;
import br.com.hioktec.api_ai.application.dto.NewChatResponse;
import br.com.hioktec.api_ai.domain.repository.MemoryChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemoryChatService {

    private final ChatClient chatClient;
    private final MemoryChatRepository memoryChatRepository;

    private static final String DEFAULT_USER_ID = "RodolfoHiOk";
    private static final String DESCRIPTION_PROMPT = "Generate a chat description based on the message, limiting the description to 30 characters: ";

    public MemoryChatService(
            ChatClient.Builder chatClientBuilder,
            JdbcChatMemoryRepository jdbcChatMemoryRepository,
            MemoryChatRepository memoryChatRepository
    ) {
        this.memoryChatRepository = memoryChatRepository;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor())
                .build();
    }

    public String sendChatMessage(String message, String chatId) {
        if (!this.memoryChatRepository.existsByChatId(chatId)) {
            throw new IllegalArgumentException("Chat ID does not exist: " + chatId);
        }

        try {
            return this.chatClient.prompt()
                    .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
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

    public NewChatResponse createNewChat(String message) {
        String description = generateChatDescription(message);
        String chatId = this.memoryChatRepository.generateChatId(DEFAULT_USER_ID, description);
        String response = this.sendChatMessage(message, chatId);
        return new NewChatResponse(chatId, description, response);
    }

    public List<Chat> getAllChats() {
        return this.memoryChatRepository.getAllChatsForUser(DEFAULT_USER_ID);
    }

    public List<ChatMessage> getChatMessages(String chatId) {
        return this.memoryChatRepository.getChatMessages(chatId);
    }

    private String generateChatDescription(String message) {
        try {
            return this.chatClient.prompt()
                    .user(DESCRIPTION_PROMPT + message)
                    .call()
                    .content();
        } catch (Exception ex) {
            if (ex instanceof NonTransientAiException) {
                return UUID.randomUUID().toString().replace("-", "");
            } else {
                throw ex;
            }
        }
    }

}
