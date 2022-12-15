package com.insiders.poc1.controller;

import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("api/poc1/customers")
@Tag(name = "Customer Controller")
public class CustomerController {

    private final ModelMapper mapper;

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a customer")
    public CustomerResponseDto save(@RequestBody @Valid CustomerRequestDto customerRequestDto){
        return customerService.save(customerRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a customer")
    public CustomerResponseDto findById(@PathVariable Long id){
        return customerService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find all customers")
    public Page<CustomerResponseDto> findAll(@PageableDefault(
            page = 0,
            size = 3,
            sort={"name"},
            direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) String name){

                // Apenas para estudo
                // O método findCustomerByName realiza esta operação.
                if(name == null) return customerService.findAll(pageable);
                else return customerService.findByName(name, pageable);
    }

    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public Page<CustomerResponseDto> findCustomerByName(@RequestParam String name,@PageableDefault(
            page = 0,
            size = 3,
            sort = {"name"},
            direction = Sort.Direction.ASC) Pageable pageable){

        return customerService.findByName(name, pageable)
                .map(n -> (mapper.map(n, CustomerResponseDto.class)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a customer")
    public void deleteById(@PathVariable Long id){
        customerService.deleteById(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a customer")
    public CustomerResponseDto update(@PathVariable Long id, @RequestBody @Valid CustomerRequestDto customerRequestDto){
        return customerService.update(customerRequestDto, id);
    }
}