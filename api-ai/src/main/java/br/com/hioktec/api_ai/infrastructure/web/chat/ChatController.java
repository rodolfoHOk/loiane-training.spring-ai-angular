package br.com.hioktec.api_ai.infrastructure.web.chat;

import br.com.hioktec.api_ai.application.dto.ChatMessage;
import br.com.hioktec.api_ai.application.dto.ErrorResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping
    ChatMessage simpleChat(@RequestBody ChatMessage message) {
        try {
            String response = this.chatClient.prompt()
                    .user(message.message())
                    .call()
                    .content();
            return new ChatMessage(response);
        } catch (Exception ex) {
            if (ex instanceof NonTransientAiException) {
                return new ChatMessage(
                        new ErrorResponse(429, "Too Many Requests", "insufficient quota")
                                .toString());
            } else {
                throw ex;
            }
        }
    }
}
