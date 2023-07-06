package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    List<Film> getAll();

    void create(Film film);

    Film update(Film film);

    Optional<Film> get(long filmId);
}
