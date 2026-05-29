package com.itis.oris.skilltrade.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslateRequest {

    @NotBlank(message = "Текст обязателен")
    @Size(max = 5000)
    private String text;

    private String sourceLang = "auto";
    private String targetLang = "ru";
}
