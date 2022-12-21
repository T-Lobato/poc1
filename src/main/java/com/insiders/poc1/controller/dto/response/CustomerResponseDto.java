package com.insiders.poc1.controller.dto.response;

import com.insiders.poc1.entities.Address;
import java.util.List;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerResponseDto {

    private Long id;
    private String name;
    private String email;
    private List<Address> addressList;

    @Version
    private Long version;
}