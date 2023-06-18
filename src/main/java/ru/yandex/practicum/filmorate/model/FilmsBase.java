package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class FilmsBase { // класс для хранения базы фильмов
    private long id;
    private HashMap<Long, Film> films = new HashMap<>();

    public long generateId() {
        return ++id;
    }
}
