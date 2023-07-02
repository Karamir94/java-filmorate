package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmRepository implements FilmRepository { // класс для хранения базы фильмов
    private long id;
    private final Map<Long, Film> films = new HashMap<>();

    private long generateId() {
        return ++id;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> get(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public void create(Film film) {
        if (films.containsValue(film)) {
            log.info("Данный фильм уже зарегестрирован");
            throw new AlreadyExistException(String.format("Film № %d already exist", film.getId()));
        } else {
            film.setId(generateId());
            films.put(film.getId(), film);

            log.debug("Текущее количество фильмов: {}", films.size());
        }
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("фильм обновлен");
        } else {
            log.debug("фильм не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return films.get(film.getId());
    }
}
