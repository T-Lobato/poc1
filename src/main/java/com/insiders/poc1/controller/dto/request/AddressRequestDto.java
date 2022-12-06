package com.insiders.poc1.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressRequestDto {

    private Long customerRef;
    private String state;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private String zipCode;
}