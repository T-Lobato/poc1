package com.insiders.poc1.shared;

import com.insiders.poc1.shared.dto.ErrorValidationDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class ValidationExceptionHandler {

    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public List<ErrorValidationDto> validationException(MethodArgumentNotValidException exception) {
        List<ErrorValidationDto> errorDtoList = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getFieldErrors();

        fieldErrors.forEach(e -> {
                    String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
                    ErrorValidationDto errorValidationDto = new ErrorValidationDto(e.getField(), message);
                    errorDtoList.add(errorValidationDto);
        });
        return errorDtoList;
    }
}
