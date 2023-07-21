package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBRatingMPARepository implements RatingMPARepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<RatingMPA> getById(int id) {
        String sql = "select * from RATING_MPA where RATING_ID = :id";
        List<RatingMPA> ratings = jdbcOperations.query(sql, Map.of("id", id), (rs, rowNum) -> makeRatingMPA(rs));
        if (!ratings.isEmpty()) {
            log.info("Найден рейтинг с id: {} и названием {} ", ratings.get(0).getId(), ratings.get(0).getName());
            return Optional.of(ratings.get(0));
        } else {
            log.info("Рейтинг с id: {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public List<RatingMPA> getAll() {
        String sql = "select * from RATING_MPA";
        List<RatingMPA> ratings = jdbcOperations.query(sql, (rs, rowNum) -> makeRatingMPA(rs));
        log.info("Найдено {} рейтингов", ratings.size());
        return ratings;
    }

    @Override
    public RatingMPA createNewRatingMPA(RatingMPA rating) {
        String sql = "INSERT INTO RATING_MPA (NAME) VALUES (:name)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", rating.getName());

        jdbcOperations.update(sql, map, keyHolder);
        rating.setId(keyHolder.getKey().intValue());
        log.info("Внесен новый рейтинг {} c id {}", rating.getName(), rating.getId());
        return rating;
    }

    private RatingMPA makeRatingMPA(ResultSet rs) throws SQLException {
        return new RatingMPA(rs.getInt("RATING_ID"), rs.getString("NAME"));
    }
}
