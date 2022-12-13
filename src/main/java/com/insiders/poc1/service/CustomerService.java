package com.insiders.poc1.service;

import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.repository.CustomerRepository;
import jakarta.transaction.Transactional;
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
    public Customer save(CustomerRequestDto customerRequestDto) {
        return customerRepository.save(mapper.map(customerRequestDto, Customer.class));
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found!"));
    }

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional
    public void deleteById(Long id) {
        Customer customer = this.findById(id);
        customerRepository.delete(customer);
    }
    @Transactional
    public Customer update(CustomerRequestDto customerRequestDto, Long id){
        Customer customer = findById(id);
        mapper.map(customerRequestDto, customer);
        return customer;
    }
}