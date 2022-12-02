package com.insiders.poc1.controller.dto.request;


import com.insiders.poc1.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {

    private Long id;
    private String name;
    private String document;
    private DocumentType documentType;
    private String email;
    private String phoneNumber;
}