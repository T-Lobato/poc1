package com.insiders.poc1.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ErrorValidationDto {

    private String field;
    private String message;
}
