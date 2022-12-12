package com.insiders.poc1.shared;

import com.insiders.poc1.exception.AddressLimitExceededException;
import com.insiders.poc1.exception.MainAddressDeleteException;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.shared.dto.ErrorMessageDto;
import com.insiders.poc1.shared.dto.ErrorMessageEnumDto;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public ErrorMessageDto resourceNotFoundExceptionHandler(RuntimeException exception, WebRequest request){
        return new ErrorMessageDto(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AddressLimitExceededException.class, MainAddressDeleteException.class})
    public ErrorMessageDto addressRulesExceptionHandler(RuntimeException exception, WebRequest request){
        return new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ErrorMessageEnumDto httpMessageNotReadableException() {
        return new ErrorMessageEnumDto(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid value entered, please enter one of the following values: [PF, PJ]",
                LocalDateTime.now()
        );
    }
}