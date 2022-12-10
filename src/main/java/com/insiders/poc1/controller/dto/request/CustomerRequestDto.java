package com.insiders.poc1.controller.dto.request;

import com.insiders.poc1.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {

    @NotNull @NotEmpty @Length(min = 3, max = 255)
    private String name;

    private String document; // TODO - Validação para CPF e CNPJ ao mesmo tempo.
    private PersonType personType; // TODO - Validação para ENUM.

    @NotNull @NotEmpty @Email
    private String email;

    @Length(min = 10, max = 11)
    private String phoneNumber; // TODO - Verificar a viabilidade para máscara em número de telefone.
}