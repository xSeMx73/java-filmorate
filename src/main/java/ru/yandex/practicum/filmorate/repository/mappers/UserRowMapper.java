package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
public class UserRowMapper implements RowMapper<User> {
    protected final JdbcTemplate jdbc;
    private static final String FIND_FRIENDS = "SELECT FRIEND_ID FROM FRIENDSHIPS where USER_ID = ?";

    public UserRowMapper(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setEmail(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("NAME"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        List<Long> friends = jdbc.query(FIND_FRIENDS, (rs1, rowNum1) -> {
            long num = rs1.getLong("FRIEND_ID");
            return num;
        }, user.getId());
        for (Long id : friends) {
            user.getFriends().add(id);
        }
        return user;
    }
}

