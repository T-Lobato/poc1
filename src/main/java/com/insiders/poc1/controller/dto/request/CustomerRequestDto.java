package com.insiders.poc1.controller.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {

    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String phoneNumber;
}