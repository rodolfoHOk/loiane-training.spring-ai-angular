package br.com.hioktec.api_ai.infrastructure.repository.chat;

import br.com.hioktec.api_ai.application.dto.Chat;
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

}
