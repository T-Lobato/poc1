package com.insiders.poc1.service;

import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.repository.CustomerRepository;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    @Transactional
    public CustomerResponseDto save(CustomerRequestDto customerRequestDto) {
        Customer customer = customerRepository.save(mapper.map(customerRequestDto, Customer.class));
        return mapper.map(customer, CustomerResponseDto.class);
    }

    public CustomerResponseDto findById(Long id) {
        return mapper.map(customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found!")), CustomerResponseDto.class);
    }

    public Page<CustomerResponseDto> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(n -> (mapper.map(n, CustomerResponseDto.class)));
    }

    @Transactional
    public void deleteById(Long id) {
        Customer customer = mapper.map(this.findById(id), Customer.class);
        customerRepository.delete(customer);
    }
    @Transactional
    public CustomerResponseDto update(CustomerRequestDto customerRequestDto, Long id){
        Customer customer = mapper.map(findById(id), Customer.class);
        mapper.map(customerRequestDto, customer);
        customerRepository.save(customer);
        return mapper.map(customer, CustomerResponseDto.class);
    }
}