package com.insiders.poc1.shared.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ErrorMessageDto {
    private Integer status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
