package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBUserRepository implements UserRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<User> getAll() {
        final String sql = "SELECT * FROM USERS ";
        List<User> users = jdbcOperations.query(sql, (rs, rowNum) -> makeUser(rs));
        log.info("Найдено {} пользователей", users.size());
        return users;
    }

    @Override
    public Optional<User> get(long id) {
        final String sql = "SELECT * FROM USERS WHERE USER_ID = :id";
        final List<User> users = jdbcOperations.query(sql, Map.of("id", id), (rs, rowNum) -> makeUser(rs));

        if (!users.isEmpty()) {
            log.info("Найден пользователь с id: {} и именем {} ", users.get(0).getId(), users.get(0).getName());
            return Optional.of(users.get(0));
        } else {
            log.info("Пользователь c id {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (:email, :login, :name, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());

        jdbcOperations.update(sql, map, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return get(user.getId()).get();
    }

    @Override
    public Optional<User> update(User user) {
        String sql = "UPDATE USERS SET EMAIL = :email, LOGIN = :login, NAME = :name, " +
                "BIRTHDAY = :birthday WHERE USER_ID = :id";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        map.addValue("id", user.getId());

        jdbcOperations.update(sql, map);

        return get(user.getId());
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
        return user;
    }
}
