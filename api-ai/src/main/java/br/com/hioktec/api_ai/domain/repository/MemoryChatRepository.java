package br.com.hioktec.api_ai.domain.repository;

import br.com.hioktec.api_ai.application.dto.Chat;

import java.util.List;

public interface MemoryChatRepository {

    String generateChatId(String userId, String description);

    List<Chat> getAllChatsForUser(String userId);

}
