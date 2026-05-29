package com.itis.oris.skilltrade.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {

    @NotNull(message = "Укажите получателя отзыва")
    private Long targetUserId;

    @NotBlank(message = "Текст отзыва обязателен")
    @Size(min = 5, max = 2000)
    private String text;

    @NotNull(message = "Оценка обязательна")
    @Min(value = 1, message = "Оценка от 1 до 5")
    @Max(value = 5, message = "Оценка от 1 до 5")
    private Integer rating;
}
