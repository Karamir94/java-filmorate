package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.repository.film.RatingMPARepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingMPAService {

    private final RatingMPARepository ratingMPARepository;

    public List<RatingMPA> findAll() {
        return ratingMPARepository.getAll();
    }

    public RatingMPA findById(int id) {
        checkId(id);
        return ratingMPARepository.getById(id)
                .orElseThrow(() -> new NotFoundException("рейтинга с id " + id + " нет в базе"));
    }

    private void checkId(long userId) {
        if (userId <= 0) {
            throw new NotFoundException("id must be positive");
        }
    }

    public RatingMPA create(RatingMPA ratingMPA) {
        return ratingMPARepository.createNewRatingMPA(ratingMPA);
    }
}
