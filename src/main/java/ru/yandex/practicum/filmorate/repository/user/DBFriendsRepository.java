package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBFriendsRepository implements FriendsRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addFriend(long userId, long friendId) {
        String sql = "INSERT INTO USERS_FRIENDS (USER_ID, FRIEND_ID) VALUES (:userId, :friendId)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("friendId", friendId);

        jdbcOperations.update(sql, map);
        log.info("Пользователь с id {} добавил в свои друзья пользователя c id {}", userId, friendId);
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM USERS_FRIENDS WHERE USER_ID = :userId AND FRIEND_ID = :friendId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("friendId", friendId);

        jdbcOperations.update(sql, map);
        int count = jdbcOperations.update(sql, map);
        if (count > 0) {
            log.info("Пользователь с id {} удалил из свои друзей пользователя c id {}", userId, friendId);
            return true;
        } else {
            log.info("У пользователя с id {} в друзьях нет пользователя c id {}", userId, friendId);
            return false;
        }
    }

    @Override
    public List<User> getUsersFriends(long userId) {
        String sql = "SELECT U.* " +
                "FROM USERS_FRIENDS AS F " +
                "JOIN USERS AS U ON F.FRIEND_ID = U.USER_ID" +
                " WHERE F.USER_ID = :userId";
        List<User> usersFriends = jdbcOperations.query(sql, Map.of("userId", userId), (rs, rowNum) -> makeUser(rs));
        log.info("У пользователя {} найдено {} друзей", userId, usersFriends.size());
        return usersFriends;
    }

    @Override
    public List<User> getCommonFriends(long user1Id, long user2Id) {
        String sql = "SELECT * " +
                "FROM USERS " +
                "WHERE USER_ID IN (" +
                "SELECT FRIEND_ID " +
                "FROM USERS_FRIENDS " +
                "WHERE USER_ID = :user1Id " +
                "INTERSECT SELECT FRIEND_ID " +
                "FROM USERS_FRIENDS " +
                "WHERE USER_ID = :user2Id)";
        List<User> usersFriends = jdbcOperations.query(
                sql,
                Map.of("user1Id", user1Id, "user2Id", user2Id),
                (rs, rowNum) -> makeUser(rs)
        );
        log.info("У пользователя {} и пользователя {} найдено {} общих друзей", user1Id, user2Id, usersFriends.size());
        return usersFriends;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }
}
