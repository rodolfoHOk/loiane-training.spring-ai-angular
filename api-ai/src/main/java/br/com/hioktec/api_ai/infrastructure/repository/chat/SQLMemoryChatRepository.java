package br.com.hioktec.api_ai.infrastructure.repository.chat;

import br.com.hioktec.api_ai.domain.repository.MemoryChatRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

}
