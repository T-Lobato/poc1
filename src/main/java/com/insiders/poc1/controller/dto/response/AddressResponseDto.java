package com.insiders.poc1.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {

    private Long id;
    private String state;
    private String city;
    private String district;
    private String street;
    private String number;
    private String zipCode;



}