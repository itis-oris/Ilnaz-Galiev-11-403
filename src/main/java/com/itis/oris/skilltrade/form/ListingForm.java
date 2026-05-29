package com.itis.oris.skilltrade.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingForm {

    @NotBlank(message = "Заголовок обязателен")
    @Size(min = 3, max = 200)
    private String title;

    @NotBlank(message = "Описание обязательно")
    @Size(min = 10, max = 4000)
    private String description;

    @NotBlank(message = "Укажите навык, которым готовы поделиться")
    @Size(max = 100)
    private String canTeach;

    @NotBlank(message = "Укажите навык, который хотите изучить")
    @Size(max = 100)
    private String wantToLearn;

    private Long categoryId;
}
