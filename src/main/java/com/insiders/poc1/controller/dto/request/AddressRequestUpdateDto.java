package com.insiders.poc1.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class AddressRequestUpdateDto {

    private Long customerRef;

    private String state;
    private String city;
    private String district;
    private String street;

    @NotBlank(message = "houseNumber is a required field!")
    private String houseNumber;

    @NotBlank(message = "zipCode is a required field!")
    @Length(min = 8, max = 8)
    private String zipCode;
}