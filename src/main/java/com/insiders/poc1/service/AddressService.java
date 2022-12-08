package com.insiders.poc1.service;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.request.MainAddressRequestDto;
import com.insiders.poc1.entities.Address;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    private final CustomerService customerService;

    private final ModelMapper mapper;

    @Transactional
    public Address save(AddressRequestDto addressRequestDto) {
        Customer customer = customerService.findById(addressRequestDto.getCustomer().getId());
        Address address = mapper.map(addressRequestDto, Address.class);

        if (customer.getAddressList().isEmpty()) {
            address.setMainAddress(true);
        }

        if (customer.getAddressList().size() < 5) {
            address.setCustomer(customer);
            return addressRepository.save(address);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "cannot add an address, this customer's address limit has been exceeded");
        }
    }

    @Transactional
    public Address update(AddressRequestDto addressRequestDto, Long id) {
        Address address = findById(id);
        address.setState(addressRequestDto.getState());
        address.setCity(addressRequestDto.getCity());
        address.setDistrict(addressRequestDto.getDistrict());
        address.setStreet(addressRequestDto.getStreet());
        address.setHouseNumber(addressRequestDto.getHouseNumber());
        address.setZipCode(addressRequestDto.getZipCode());
        return addressRepository.save(address);
    }

    public Address findById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not Found!"));
    }

    @Transactional
    public void updateMainAddress(MainAddressRequestDto mainAddressRequestDto) {
        Address address = findById(mainAddressRequestDto.getId());

        address.getCustomer().getAddressList()
                .forEach(n -> {
                    n.setMainAddress(false);
                    addressRepository.save(n);
                });

        address.setMainAddress(true);
        addressRepository.save(address);
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            Address address = findById(id);
            if(!address.isMainAddress()) {
                addressRepository.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The main address can't be deleted");
            }
        }
        catch (ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Address not found!");
        }

    }
}