package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;
import ru.yandex.practicum.filmorate.repository.film.LikesRatingRepository;
import ru.yandex.practicum.filmorate.repository.film.RatingMPARepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final LikesRatingRepository likesRatingRepository;
    private final GenreRepository genreRepository;
    private final RatingMPARepository ratingMPARepository;

    public List<Film> getAll() {
        List<Film> films = filmRepository.getAll();
        genreRepository.load(films);
        return films;
    }

    public Film get(long filmId) {
        checkId(filmId);
        Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Film № %d not found", filmId)));

        genreRepository.load(List.of(film));
        return film;
    }

    private Film setFilmGenres(Film film) {
        if (film.getGenres() != null) {
            StringBuilder sb = new StringBuilder();
            if (film.getGenres().isEmpty()) {
                sb.append("DELETE FROM FILM_GENRES WHERE FILM_ID = " + film.getId() + " ");
            } else {
                List<Integer> filmGenres = film.getGenres().stream()
                        .map(Genre::getId)
                        .distinct()
                        .collect(Collectors.toList());

                sb.append("DELETE FROM FILM_GENRES WHERE FILM_ID = " + film.getId()
                        + " ; " + "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES ");

                for (Integer genreId : filmGenres) {
                    sb.append("( " + film.getId() + ", " + genreId + " ), ");
                }
                int length = sb.length();
                sb.deleteCharAt(length - 2);
            }

            String sql = sb.toString();
            genreRepository.updateGenresForFilm(sql);

            film.setGenres(new LinkedHashSet<>());
            genreRepository.load(List.of(film));
        }
        return film;
    }

    public Film create(Film film) {
        Film creFilm = filmRepository.create(film);
        return setFilmGenres(creFilm);
    }

    public Film update(Film film) {
        checkId(film.getId());
        Film updFilm = filmRepository.update(film);
        updFilm.setMpa(ratingMPARepository.getById(film.getMpa().getId()).get());
        return setFilmGenres(updFilm);
    }

    public void addLike(long filmId, long userId) {
        checkId(userId);
        checkId(filmId);

        if (likesRatingRepository.checkUserLikedFilm(filmId, userId)) {
            log.info("У фильма с id {} уже есть лайк от пользователя {}", filmId, userId);
            throw new AlreadyExistException(String
                    .format("У фильма с id %d уже есть лайк от пользователя %d ", filmId, userId));
        } else {
            likesRatingRepository.userLikedFilm(filmId, userId);
            log.info("Фильму с id {} добавлен лайк от пользователя {}", filmId, userId);
        }
    }

    public void deleteLike(long filmId, long userId) {
        checkId(userId);
        checkId(filmId);

        if (likesRatingRepository.checkUserLikedFilm(filmId, userId)) {
            likesRatingRepository.deleteLike(filmId, userId);
            log.info("У фильма с id {} удален лайк от пользователя с id {}", filmId, userId);
        } else {
            log.info("У фильма с id {} нет лайка от пользователя с id {}", filmId, userId);
            throw new NotFoundException(String
                    .format("У фильма с id {} нет лайка от пользователя с id {}", filmId, userId));
        }
    }

    private void checkId(long userId) {
        if (userId <= 0) {
            throw new NotFoundException("id must be positive");
        }
    }

    public List<Film> getTopFilms(int size) {
        List<Film> films = filmRepository.getPopularFilmList(size);

        for (Film film : films) {
            film.setGenres(new LinkedHashSet<>(genreRepository.getFilmGenre(film.getId())));
        }
        return films;
    }
}
