package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Getter
@Setter
public class FilmsRepository { // класс для хранения базы фильмов
    private long id;
    private HashMap<Long, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger("FilmsRepository");

    public long generateId() {
        return ++id;
    }

    public void create(Film film) throws UserAlreadyExistException {
        if (films.containsValue(film)) {
            log.info("Данный фильм уже зарегестрирован");
            throw new UserAlreadyExistException();
        } else {
            film.setId(generateId());
            films.put(film.getId(), film);

            log.debug("Текущее количество фильмов: {}", films.size());
        }
    }

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
