package com.insiders.poc1.controller;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.request.MainAddressRequestDto;
import com.insiders.poc1.controller.dto.response.AddressResponseDto;
import com.insiders.poc1.service.AddressService;
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
public class AddressController {

    private final ModelMapper mapper;
    private final AddressService addressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponseDto save(@RequestBody AddressRequestDto addressRequestDto){
        return mapper.map(addressService.save(addressRequestDto), AddressResponseDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponseDto update(@PathVariable Long id, @RequestBody AddressRequestDto addressRequestDto){
        return mapper.map(addressService.update(addressRequestDto, id), AddressResponseDto.class);
    }

    @PatchMapping("/turn-into-main-address/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponseDto updateMainAddress(@PathVariable Long id){
        var mainAddressRequestDto = new MainAddressRequestDto(id);
        addressService.updateMainAddress(mainAddressRequestDto);
        return mapper.map(addressService.findById(id), AddressResponseDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponseDto findById(@PathVariable Long id){
        return mapper.map(addressService.findById(id), AddressResponseDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        addressService.deleteById(id);
    }
}