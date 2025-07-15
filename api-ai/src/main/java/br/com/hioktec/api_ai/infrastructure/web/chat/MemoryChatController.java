package br.com.hioktec.api_ai.infrastructure.web.chat;

import br.com.hioktec.api_ai.application.dto.ChatMessage;
import br.com.hioktec.api_ai.application.dto.NewChatResponse;
import br.com.hioktec.api_ai.application.service.MemoryChatService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-memory")
public class MemoryChatController {

    private final MemoryChatService memoryChatService;

    public MemoryChatController(MemoryChatService memoryChatService) {
        this.memoryChatService = memoryChatService;
    }

    @PostMapping("/{chatId}")
    ChatMessage simpleChat(@PathVariable String chatId, @RequestBody ChatMessage message) {
        String response = this.memoryChatService.chat(message.message(), chatId);
        return new ChatMessage(response);
    }

    @PostMapping("/start")
    NewChatResponse newChat(@RequestBody ChatMessage chatMessage) {
        return this.memoryChatService.createNewChat(chatMessage.message());
    }

}
