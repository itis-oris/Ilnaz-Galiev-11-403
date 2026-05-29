package com.itis.oris.skilltrade.form;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileForm {

    @Size(max = 1000)
    private String bio;

    @Size(max = 100)
    private String city;
}
