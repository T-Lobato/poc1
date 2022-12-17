package com.insiders.poc1.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class AddressRequestDto {

    private Long customerRef;

//    @NotBlank(message = "state is a required field!")
    private String uf;

//    @NotBlank(message = "city is a required field!")
    private String localidade;

//    @NotBlank(message = "district is a required field!")
    private String bairro;

//    @NotBlank(message = "street is a required field!")
    private String logradouro;

    @NotBlank(message = "houseNumber is a required field!")
    private String houseNumber;

    @NotBlank(message = "zipCode is a required field!")
    @Length(min = 8, max = 8)
    private String cep;

    @Override
    public String toString() {
        return "AddressRequestDto{" +
                "customerRef=" + customerRef +
                ", uf='" + uf + '\'' +
                ", localidade='" + localidade + '\'' +
                ", bairro='" + bairro + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", cep='" + cep + '\'' +
                '}';
    }
}