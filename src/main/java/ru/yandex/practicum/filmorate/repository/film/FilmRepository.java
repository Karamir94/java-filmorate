package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Optional<Film> get(long filmId);

    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    List<Film> getPopularFilmList(int count);
}
