package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.RealiseDateConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    @EqualsAndHashCode.Exclude
    private long id;
    @NotBlank
    private String name; // название не может быть пустым
    @Size(max = 200, message = "Максимальная длина описания 200 символов")
    private String description; // максимальная длина описания — 200 символов
    @RealiseDateConstraint // custom annotation
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate; // дата релиза — не раньше 28 декабря 1895 года
    @Positive
    private int duration; // продолжительность фильма должна быть положительной
    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    public Set<Long> getLikes() {
        return likes;
    }

    public void setLikes(Set<Long> likes) {
        this.likes = likes;
    }

    public int getLikesAmount() {
        return likes.size();
    }
}