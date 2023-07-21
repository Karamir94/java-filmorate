package ru.yandex.practicum.filmorate.repository.film;

public interface LikesRatingRepository {
    void userLikedFilm(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    boolean checkUserLikedFilm(long filmId, long userId);
}
