package com.insiders.poc1.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class AddressRequestUpdateDto {

    @NotBlank(message = "houseNumber is a required field!")
    private String houseNumber;

    private String complement;

    @NotBlank(message = "zipCode is a required field!")
    @Length(min = 8, max = 8)
    @Pattern(regexp = "^[^\\D]{8}$", message = "this field only accepts numbers.")
    private String cep;
}