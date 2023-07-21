package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<Genre> getById(int id) {
        String sql = "select * from GENRES where GENRE_ID = :id";
        List<Genre> genres = jdbcOperations.query(sql, Map.of("id", id), (rs, rowNum) -> makeGenre(rs));
        if (!genres.isEmpty()) {
            log.info("Найден жанр с id: {} и названием {} ", genres.get(0).getId(), genres.get(0).getName());
            return Optional.of(genres.get(0));
        } else {
            log.info("Жанр с id: {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAll() {
        String sql = "select * from GENRES";
        List<Genre> genres = jdbcOperations.query(sql, (rs, rowNum) -> makeGenre(rs));
        log.info("Найдено {} жанров", genres.size());
        return genres;
    }

    @Override
    public List<Genre> getFilmGenre(long filmId) {
        String sql = "SELECT G.GENRE_ID, G.NAME "
                + "FROM FILM_GENRES AS FG "
                + "JOIN GENRES AS G on FG.GENRE_ID = G.GENRE_ID "
                + "WHERE FG.FILM_ID = :filmId "
                + "ORDER BY G.GENRE_ID";
        List<Genre> genres = jdbcOperations.query(sql, Map.of("filmId", filmId), (rs, rowNum) -> makeGenre(rs));
        log.info("Для фильма {} найдено жанров {}", filmId, genres.size());
        return genres;
    }

    @Override
    public Genre createNewGenre(Genre genre) {
        String sql = "insert into GENRES (NAME) VALUES (:name)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", genre.getName());

        jdbcOperations.update(sql, map, keyHolder);
        genre.setId(keyHolder.getKey().intValue());
        log.info("Внесен новый жанр {} c id {}", genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public void setFilmGenre(long filmId, int genreId) {
        String sql = "INSERT INTO FILM_GENRES(FILM_ID, GENRE_ID) VALUES (:filmId, :genreId)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        map.addValue("genreId", genreId);

        jdbcOperations.update(sql, map);
        log.info("Добавлен жанр с id: {} для фильма c id {}", genreId, filmId);
    }

    @Override
    public boolean deleteGenresOfFilm(long id) {
        String sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);

        int count = jdbcOperations.update(sql, map);
        log.info("Удалено жанров {} для фильма c id {}", count, id);
        return count > 0;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }
}
