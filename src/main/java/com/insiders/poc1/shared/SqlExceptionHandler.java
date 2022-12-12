package com.insiders.poc1.shared;

import com.insiders.poc1.shared.dto.ErrorMessageDto;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class SqlExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ErrorMessageDto sQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception, WebRequest request){
        return new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                errorMessageConfig(exception),
                request.getDescription(false),
                LocalDateTime.now()


        );
    }

    // TODO - Verificar se é correto retornar os dados dessa maneira.
    public String errorMessageConfig(SQLIntegrityConstraintViolationException exception){
        String[] error = exception.getMessage().split(" ");
        return error[2] + " is already registered in our database.";
    }

}