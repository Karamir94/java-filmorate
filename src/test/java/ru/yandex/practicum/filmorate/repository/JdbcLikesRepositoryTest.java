package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.repository.film.DBLikesRatingRepository;
import ru.yandex.practicum.filmorate.repository.film.LikesRatingRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Import(DBLikesRatingRepository.class)
public class JdbcLikesRepositoryTest {

    @Autowired
    private LikesRatingRepository likesRatingRepository;

    @Test
    public void shouldAddAndDeleteLike() {
        likesRatingRepository.userLikedFilm(1, 1);
        boolean checkLikeTrue = likesRatingRepository.checkUserLikedFilm(1, 1);

        assertThat(checkLikeTrue)
                .isEqualTo(true);

        likesRatingRepository.deleteLike(1, 1);

        boolean checkLikeFalse = likesRatingRepository.checkUserLikedFilm(1, 1);

        assertThat(checkLikeFalse)
                .isEqualTo(false);
    }
}
