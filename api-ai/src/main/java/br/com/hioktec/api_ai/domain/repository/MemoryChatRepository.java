package br.com.hioktec.api_ai.domain.repository;

public interface MemoryChatRepository {

    String generateChatId(String userId, String description);

}
