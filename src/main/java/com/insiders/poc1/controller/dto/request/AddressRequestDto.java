package com.insiders.poc1.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class AddressRequestDto {

    private Long customerRef;

    private String uf;
    private String localidade;
    private String bairro;
    private String logradouro;

    @NotBlank(message = "houseNumber is a required field!")
    private String houseNumber;

    @NotBlank(message = "zipCode is a required field!")
    @Length(min = 8, max = 8)
    @Pattern(regexp = "^[^\\D]{8}$", message = "this field only accepts numbers.")
    private String cep;
}