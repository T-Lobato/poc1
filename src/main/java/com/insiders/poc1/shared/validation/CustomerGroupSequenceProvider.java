package com.insiders.poc1.shared.validation;

import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

public class CustomerGroupSequenceProvider implements DefaultGroupSequenceProvider<CustomerRequestDto> {

    @Override
    public List<Class<?>> getValidationGroups(CustomerRequestDto customerRequestDto) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(CustomerRequestDto.class);

        if(isPersonSelected(customerRequestDto)) {
            groups.add(customerRequestDto.getPersonType().getGroup());
        }
        return groups;

    }

    protected boolean isPersonSelected(CustomerRequestDto customerRequestDto) {
        return customerRequestDto != null && customerRequestDto.getPersonType() != null;
    }

}