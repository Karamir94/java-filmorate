package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBLikesRatingRepository implements LikesRatingRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void userLikedFilm(long filmId, long userId) {
        String sql = "INSERT INTO FILM_RATE (FILM_ID, USER_ID_LIKES) VALUES (:filmId, :userId)";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        map.addValue("userId", userId);

        jdbcOperations.update(sql, map);
        log.info("Фильму с id: {} добавлен лайк от пользователя c id {}", filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM FILM_RATE WHERE FILM_ID = :filmId AND USER_ID_LIKES = :userId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        map.addValue("userId", userId);

        jdbcOperations.update(sql, map);
        log.info("Удален лайк от пользователя с id {} для фильма c id {}", userId, filmId);
    }

    @Override
    public boolean checkUserLikedFilm(long filmId, long userId) {
        String sql = "SELECT USER_ID_LIKES FROM FILM_RATE WHERE FILM_ID = :filmId AND USER_ID_LIKES = :userId";
        List<Long> likes = jdbcOperations.query(
                sql,
                Map.of("filmId", filmId, "userId", userId),
                (rs, rowNum) -> makeRate(rs)
        );
        if (likes.size() == 0) {
            log.info("Для фильма {} не найдено лайков", filmId);
            return false;
        } else {
            log.info("Для фильма {} найдено {} лайков", filmId, likes.get(0));
            return true;
        }
    }

    private Long makeRate(ResultSet rs) throws SQLException {
        return rs.getLong("USER_ID_LIKES");
    }
}
