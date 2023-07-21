package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBFilmRepository implements FilmRepository {

    private final GenreRepository genreRepository;
    private final RatingMPARepository ratingMPARepository;
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<Film> get(long filmId) {
        String sql = "SELECT F.FILM_ID, F.NAME AS FILM_NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, " +
                "F.RATING_MPA_ID, R.NAME AS MPA_NAME " +
                "FROM FILMS AS F " +
                "JOIN RATING_MPA AS R ON F.RATING_MPA_ID = R.RATING_ID " +
                "WHERE F.FILM_ID = :filmId";

        List<Film> films = jdbcOperations.query(sql, Map.of("filmId", filmId), (rs, rowNum) -> makeFilm(rs));
        if (!films.isEmpty()) {
            log.info("Найдена фильм с id: {} и названием {} ", films.get(0).getId(), films.get(0).getName());
            return Optional.of(films.get(0));
        } else {
            log.info("Фильм c id {} не найден", filmId);
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT F.FILM_ID, F.NAME AS FILM_NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, " +
                "F.RATING_MPA_ID, R.NAME AS MPA_NAME " +
                "FROM FILMS AS F " +
                "JOIN RATING_MPA AS R ON F.RATING_MPA_ID = R.RATING_ID ";

        List<Film> films = jdbcOperations.query(sql, (rs, rowNum) -> makeFilm(rs));
        log.info("Найдено {} фильмов", films.size());
        return films;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILMS (NAME, RELEASE_DATE, DESCRIPTION, RATING_MPA_ID, DURATION) " +
                "VALUES (:name, :releaseDate, :description, :ratingMpaId, :duration)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", film.getName());
        map.addValue("releaseDate", film.getReleaseDate());
        map.addValue("description", film.getDescription());
        map.addValue("ratingMpaId", film.getMpa().getId());
        map.addValue("duration", film.getDuration());

        jdbcOperations.update(sql, map, keyHolder);
        Long filmId = keyHolder.getKey().longValue();

        film.setId(filmId);
        List<Integer> filmGenres = film.getGenres().stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());
        for (Integer genreId : filmGenres) {
            genreRepository.setFilmGenre(film.getId(), genreId);
        }
        film.setGenres(genreRepository.getFilmGenre(filmId));
        return get(film.getId()).get();
    }

    @Override
    public Optional<Film> update(Film film) {
        String sql = "UPDATE FILMS SET NAME = :name, RELEASE_DATE = :releaseDate, DESCRIPTION = :description, " +
                "RATING_MPA_ID = :ratingMpaId, DURATION = :duration " +
                "WHERE FILM_ID = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", film.getId());
        map.addValue("name", film.getName());
        map.addValue("releaseDate", film.getReleaseDate());
        map.addValue("description", film.getDescription());
        map.addValue("ratingMpaId", film.getMpa().getId());
        map.addValue("duration", film.getDuration());

        jdbcOperations.update(sql, map);

        List<Integer> filmGenreIdList = film.getGenres().stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());
        genreRepository.deleteGenresOfFilm(film.getId());

        for (Integer genreId : filmGenreIdList) {
            genreRepository.setFilmGenre(film.getId(), genreId);
        }
        film.setGenres(genreRepository.getFilmGenre(film.getId()));
        film.setMpa(ratingMPARepository.getById(film.getMpa().getId()).get());
        return get(film.getId());
    }

    @Override
    public List<Film> getPopularFilmList(int size) {
        String sql = "SELECT F.FILM_ID, F.NAME AS FILM_NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, " +
                "F.RATING_MPA_ID, R.NAME AS MPA_NAME, COUNT(FR.USER_ID_LIKES) AS RT " +
                "FROM FILMS AS F " +
                "JOIN RATING_MPA AS R ON F.RATING_MPA_ID = R.RATING_ID " +
                "LEFT JOIN FILM_RATE AS FR ON F.FILM_ID = FR.FILM_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY RT DESC " +
                "LIMIT :size";
        List<Film> films = jdbcOperations.query(sql, Map.of("size", size), (rs, rowNum) -> makeFilm(rs));
        log.info("По запросу на {} самых популярных фильмов сформиован список из {} фильмов", size, films.size());
        return films;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        long filmId = rs.getLong("FILM_ID");

        Film film = new Film(
                filmId,
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new RatingMPA(rs.getInt("RATING_MPA_ID"), rs.getString("MPA_NAME")),
                genreRepository.getFilmGenre(filmId)
        );
        return film;
    }
}
