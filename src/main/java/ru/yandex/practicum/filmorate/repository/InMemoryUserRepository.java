package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserRepository implements UserRepository { // // класс для хранения базы пользователей
    private long id;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    private long generateId() {
        return ++id;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void create(User user) {
        if (users.containsValue(user)) {
            log.debug("Данный пользователь уже зарегестрирован");
            throw new AlreadyExistException(String.format("User № %d already exist", user.getId()));
        } else {
            user.setId(generateId());
            users.put(user.getId(), user);
            log.debug("Текущее количество пользователей: {}", users.size());
        }
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return users.get(user.getId());
    }

    @Override
    public Optional<User> get(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void addFriend(long userId, long friendId) {
        final Set<Long> userFriends = friends.computeIfAbsent(userId, k -> new HashSet<>());
        final Set<Long> friendFriends = friends.computeIfAbsent(friendId, k -> new HashSet<>());

        userFriends.add(friendId);
        friendFriends.add(userId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        if (!friends.containsKey(userId)) {
            throw new NotFoundException(String.format("User № %d has not friends", userId));
        }
        if (!friends.containsKey(friendId)) {
            throw new NotFoundException(String.format("User № %d has not friends", friendId));
        }

        friends.get(userId).remove(friendId);
        friends.get(friendId).remove(userId);
    }

    @Override
    public Set<Long> getFriends(long userId) {
        return friends.getOrDefault(userId, new HashSet<>());
    }
}
