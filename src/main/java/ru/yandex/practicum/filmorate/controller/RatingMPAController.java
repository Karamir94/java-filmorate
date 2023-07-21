package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.RatingMPAService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class RatingMPAController {

    private final RatingMPAService ratingMPAService;

    @GetMapping
    public List<RatingMPA> getAll() {
        log.info("Получен GET запрос");
        return ratingMPAService.findAll();
    }

    @GetMapping("/{id}")
    public RatingMPA get(@PathVariable int id) {
        log.info("Получен GET запрос");
        return ratingMPAService.findById(id);
    }

    @PostMapping
    public RatingMPA create(@RequestBody RatingMPA ratingMPA) {
        log.info("Получен POST запрос");
        return ratingMPAService.create(ratingMPA);
    }
}
