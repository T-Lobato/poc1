package com.insiders.poc1.controller;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.response.AddressResponseDto;
import com.insiders.poc1.service.AddressService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/poc1/address")
public class AddressController {

    private final ModelMapper mapper;

    private final AddressService addressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponseDto save(@RequestBody AddressRequestDto addressRequestDto) {
        return mapper.map(addressService.save(addressRequestDto), AddressResponseDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponseDto update(@PathVariable Long id, @RequestBody AddressRequestDto addressRequestDto) {
        return mapper.map(addressService.update(addressRequestDto, id), AddressResponseDto.class);
    }
}