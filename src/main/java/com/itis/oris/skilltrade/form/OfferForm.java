package com.itis.oris.skilltrade.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferForm {

    @NotNull(message = "listingId обязателен")
    private Long listingId;

    @NotBlank(message = "Сообщение обязательно")
    @Size(min = 5, max = 2000)
    private String message;
}
