package com.insiders.poc1.controller.dto.request;

import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.shared.validation.CnpjGroup;
import com.insiders.poc1.shared.validation.CpfGroup;
import com.insiders.poc1.shared.validation.CustomerGroupSequenceProvider;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequenceProvider(CustomerGroupSequenceProvider.class)
public class CustomerRequestDto implements Serializable {

    @NotNull @NotEmpty @Length(min = 3, max = 255)
    private String name;

    @NotBlank(message = "\"CPF/CNPJ is a required field.\"")
    @CPF(groups = CpfGroup.class)
    @CNPJ(groups = CnpjGroup.class)
    private String document;

    @NotNull
    private PersonType personType;

    @NotNull @NotEmpty @Email
    private String email;

    @NotNull @NotEmpty
    @Length(min = 10, max = 11)
    private String phoneNumber; //TODO mudar variável para Integer
}