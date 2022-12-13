package com.insiders.poc1.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class AddressRequestDto {

    private Long customerRef;

    @NotBlank(message = "state is a required field!")
    private String state;

    @NotBlank(message = "city is a required field!")
    private String city;

    @NotBlank(message = "district is a required field!")
    private String district;

    @NotBlank(message = "street is a required field!")
    private String street;

    @NotBlank(message = "houseNumber is a required field!")
    private String houseNumber;

    @NotBlank(message = "zipCode is a required field!")
    @Length(min = 8, max = 8)
    private String zipCode; //TODO - Mudar variável para Integer
}