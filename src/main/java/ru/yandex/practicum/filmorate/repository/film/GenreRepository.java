package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    Optional<Genre> getById(int id);

    List<Genre> getAll();

    List<Genre> getFilmGenre(long filmId);

    Genre createNewGenre(Genre genre);

    void setFilmGenre(long filmId, int genreId);

    boolean deleteGenresOfFilm(long id);

    void load(List<Film> films);

    void updateGenresForFilm(Film film);
}
