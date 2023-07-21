package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.FriendsRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User get(long userId) {
        checkId(userId);
        return userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User № %d not found", userId)));
    }

    public User create(User user) {
        return userRepository.create(user);
    }

    public User update(User user) {
        checkId(user.getId());
        return userRepository.update(user);
    }

    public void addFriend(long userId, long friendId) {
        checkId(userId);
        checkId(friendId);

        if (!friendsRepository.checkUserInFriends(userId, friendId)) {
            friendsRepository.addFriend(userId, friendId);
        } else {
            log.info("у пользователя с id {} уже есть друг c id {}", userId, friendId);
            throw new AlreadyExistException(String
                    .format("у пользователя с id {} уже есть друг c id {}", userId, friendId));
        }
    }

    private void checkId(long userId) {
        if (userId <= 0) {
            throw new NotFoundException("id must be positive");
        }
    }

    public void deleteFriend(long userId, long friendId) {
        checkId(userId);
        checkId(friendId);

        if (friendsRepository.checkUserInFriends(userId, friendId)) {
            friendsRepository.deleteFriend(userId, friendId);
        } else {
            log.info("у пользователя с id {} нет друга c id {}", userId, friendId);
            throw new NotFoundException(String
                    .format("у пользователя с id {} нет друга c id {}", userId, friendId));
        }
    }

    public List<User> getFriends(long userId) {
        checkId(userId);
        return friendsRepository.getUsersFriends(userId);
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        checkId(user1Id);
        checkId(user2Id);
        return friendsRepository.getCommonFriends(user1Id, user2Id);
    }
}
