package com.itis.oris.skilltrade.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 64, message = "Логин должен быть от 3 до 64 символов")
    private String username;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    @Size(max = 128)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, max = 128, message = "Пароль должен быть от 6 до 128 символов")
    private String password;

    @Size(max = 1000, message = "Био не длиннее 1000 символов")
    private String bio;

    @Size(max = 100, message = "Название города не длиннее 100 символов")
    private String city;
}
