package ru.yandex.practicum.filmorate.repository;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM USERS";
    private static final String FIND_ONE_QUERY = "SELECT * FROM USERS where USER_ID = ?";
    protected static final String INSERT_USER_QUERY = "INSERT INTO USERS (NAME, EMAIL, LOGIN, BIRTHDAY)" +
            " values (?, ?, ?, ?)";
    private static final String SELECT_FOR_ID_QUERY = "SELECT USER_ID FROM USERS where EMAIL = ?";
    private static final String UPDATE_QUERY = "UPDATE USERS SET " +
            "NAME = ?, " +
            "EMAIL = ?, " +
            "LOGIN = ?, " +
            "BIRTHDAY = ? " +
            "WHERE USER_ID = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID)" +
            " VALUES (?, ?)";
    private static final String SELECT_FRIENDS_QUERY = "SELECT * FROM USERS " +
            "WHERE USER_ID in " +
            "(SELECT FRIEND_ID FROM FRIENDSHIPS " +
            "where USER_ID = ?) ";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM FRIENDSHIPS " +
            "WHERE USER_ID = ? " +
            "  AND FRIEND_ID = ? ";
    private static final String SELECT_COMMON_FRIENDS = "WITH one AS (\n" +
            "             SELECT FRIEND_ID\n" +
            "               FROM FRIENDSHIPS\n" +
            "              WHERE USER_ID = ?\n" +
            "            ),\n" +
            "     two AS (\n" +
            "             SELECT FRIEND_ID\n" +
            "               FROM FRIENDSHIPS\n" +
            "              WHERE USER_ID = ?\n" +
            "            )\n" +
            "   SELECT u.* \n" +
            "     FROM USERS u\n" +
            "    INNER JOIN one\n" +
            "       ON one.FRIEND_ID = u.USER_ID \n" +
            "    INNER JOIN two\n" +
            "       ON two.FRIEND_ID = u.USER_ID\n";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }


    public Collection<User> findAllUsers() {
        return super.findMany(FIND_ALL_QUERY);
    }

    public User createUser(User user) {
        super.insert(INSERT_USER_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday());
        Long id = jdbc.queryForObject(SELECT_FOR_ID_QUERY, Long.class, user.getEmail());
        user.setId(id);
        return user;
    }

    public User updateUser(User user) {
        super.update(UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    public Optional<User> getUser(Long userId) {
        return super.findOne(FIND_ONE_QUERY, userId);
    }

    public void addFriend(Long userId, Long friendId) {
        super.insert(INSERT_FRIEND_QUERY,
                userId,
                friendId);
    }

    public List<User> getUserFriends(Long id) {
        return super.findMany(SELECT_FRIENDS_QUERY, id);
    }

    public void deleteFriend(Long userId, Long friendId) {
        super.delete(DELETE_FRIEND_QUERY, userId, friendId);
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        return super.findMany(SELECT_COMMON_FRIENDS, userId, otherUserId);
    }
}
