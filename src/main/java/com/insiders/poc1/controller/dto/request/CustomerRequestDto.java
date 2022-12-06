package com.insiders.poc1.controller.dto.request;

import com.insiders.poc1.entities.Address;
import com.insiders.poc1.enums.DocumentType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {

    private Long id;
    private String name;
    private String document;
    private DocumentType documentType;
    private String email;
    private String phoneNumber;
    private List<Address> addressList;
}