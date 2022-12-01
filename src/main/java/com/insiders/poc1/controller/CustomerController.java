package com.insiders.poc1.controller;

import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.service.CustomerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("api/poc1/customers")
public class CustomerController {

    private final ModelMapper mapper;

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDto save(@RequestBody CustomerRequestDto customerRequestDto){
        return mapper.map(customerService.save(customerRequestDto), CustomerResponseDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponseDto findById(@PathVariable Long id){
        return mapper.map(customerService.findById(id), CustomerResponseDto.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponseDto> findAll(){
        return customerService.findAll()
                .stream()
                .map(n -> (mapper.map(n, CustomerResponseDto.class)))
                .collect(Collectors.toList());
    }





}