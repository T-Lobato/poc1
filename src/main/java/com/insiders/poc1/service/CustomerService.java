package com.insiders.poc1.service;

import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        customerOptional.orElseThrow(() -> new RuntimeException("Customer not found!"));
        customerRepository.deleteById(id);
    }
    @Transactional
    public Customer update(CustomerRequestDto customerRequestDto){
        Customer customer = findById(customerRequestDto.getId());
        mapper.map(customerRequestDto, customer);
        return customer;
    }
}