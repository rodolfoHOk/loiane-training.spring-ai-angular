package br.com.hioktec.api_ai.infrastructure.repository.chat;

import br.com.hioktec.api_ai.application.dto.Chat;
import br.com.hioktec.api_ai.application.dto.ChatMessage;
import br.com.hioktec.api_ai.domain.repository.MemoryChatRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SQLMemoryChatRepository implements MemoryChatRepository {

    private final JdbcTemplate jdbcTemplate;

    public SQLMemoryChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String generateChatId(String userId, String description) {
        final String sql = "INSERT INTO CHAT_MEMORY (user_id, description) VALUES (?, ?) RETURNING conversation_id";
        return jdbcTemplate.queryForObject(sql, String.class, userId, description);
    }

    @Override
    public List<Chat> getAllChatsForUser(String userId) {
        String sql = "SELECT conversation_id, description FROM CHAT_MEMORY WHERE user_id = ? ORDER BY conversation_id DESC";
        return jdbcTemplate.query(
                sql,
                (resultSet, _rowNumber) -> new Chat(
                        resultSet.getString("conversation_id"),
                        resultSet.getString("description")),
                userId
        );
    }

    @Override
    public boolean existsByChatId(String chatId) {
        String sql = "SELECT COUNT(*) FROM chat_memory WHERE conversation_id = ?::uuid";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, chatId);
        return count != null && count == 1;
    }

    @Override
    public List<ChatMessage> getChatMessages(String chatId) {
        String sql = "SELECT content, type FROM spring_ai_chat_memory WHERE conversation_id = ? ORDER BY timestamp ASC";
        return jdbcTemplate.query(
                sql,
                (resultSet, _rowNumber) -> new ChatMessage(
                        resultSet.getString("content"),
                        resultSet.getString("type")),
                chatId
        );
    }

}
