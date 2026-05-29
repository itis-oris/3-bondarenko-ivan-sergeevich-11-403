package com.itis.cryptotracker.dto.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 64, message = "Имя пользователя должно быть от 3 до 64 символов")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Имя пользователя может содержать только латиницу, цифры, '.', '_' и '-'")
    private String username;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    @Size(max = 128)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 100, message = "Пароль должен быть не короче 8 символов")
    private String password;

    @NotBlank(message = "Подтвердите пароль")
    private String passwordConfirm;

    private boolean emailNotificationsEnabled;
}
