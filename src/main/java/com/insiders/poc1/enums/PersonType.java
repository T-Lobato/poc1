package com.insiders.poc1.enums;

import com.insiders.poc1.shared.validation.CnpjGroup;
import com.insiders.poc1.shared.validation.CpfGroup;
import lombok.Getter;

@Getter
public enum PersonType {
    PF("Pessoa Física", "CPF", "000.000.000-00", CpfGroup.class),
    PJ("Pessoa Jurídica", "CNPJ", "00.000.000/0000-00", CnpjGroup.class);

    private final String description;
    private final String document;
    private final String mask;
    private final Class<?> group;

    PersonType(String description, String document, String mask, Class<?> group) {
        this.description = description;
        this.document = document;
        this.mask = mask;
        this.group = group;
    }
}