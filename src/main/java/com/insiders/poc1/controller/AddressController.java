package com.insiders.poc1.controller;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.response.AddressResponseDto;
import com.insiders.poc1.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/poc1/address")
@Tag(name = "Address Controller")
public class AddressController {

    private final ModelMapper mapper;
    private final AddressService addressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save an address")
    public AddressResponseDto save(@RequestBody @Valid AddressRequestDto addressRequestDto) throws Exception {
        return addressService.save(addressRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an address")
    public AddressResponseDto update(@PathVariable Long id, @RequestBody @Valid AddressRequestDto addressRequestDto){
        return addressService.update(addressRequestDto, id);
    }

    @PatchMapping("/turn-into-main-address/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an address to main")
    public AddressResponseDto updateMainAddress(@PathVariable Long id){
        addressService.updateMainAddress(id);
        return addressService.findById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find an address")
    public AddressResponseDto findById(@PathVariable Long id){
        return addressService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an address")
    public void deleteById(@PathVariable Long id){
        addressService.deleteById(id);
    }
}