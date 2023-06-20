package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {

    @EqualsAndHashCode.Exclude
    private long id;
    @NonNull
    @Email
    private String email; // электронная почта не может быть пустой и должна содержать символ @
    @NotBlank
    private String login; // логин не может быть пустым и содержать пробелы;
    @EqualsAndHashCode.Exclude
    @NonNull
    private String name; // имя для отображения может быть пустым — в таком случае будет использован логин;
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NonNull
    private LocalDate birthday; // дата рождения не может быть в будущем

    public void setName(String name) {
        if (name.isBlank() || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }
    }

    public String getName() {
        if (name == null) {
            name = login;
        }
        return name;
    }
}
