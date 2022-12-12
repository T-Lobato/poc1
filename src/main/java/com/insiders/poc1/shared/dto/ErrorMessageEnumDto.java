package com.insiders.poc1.shared.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ErrorMessageEnumDto {

    private Integer status;
    private String message;
    private LocalDateTime timestamp;
}