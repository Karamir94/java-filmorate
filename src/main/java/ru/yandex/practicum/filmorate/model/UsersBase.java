package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class UsersBase { // // класс для хранения базы пользователей
    private long id;
    private HashMap<Long, User> users = new HashMap<>();

    public long generateId() {
        return ++id;
    }
}
