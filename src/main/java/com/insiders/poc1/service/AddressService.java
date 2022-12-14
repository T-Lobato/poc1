package com.insiders.poc1.service;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.response.AddressResponseDto;
import com.insiders.poc1.entities.Address;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.exception.AddressLimitExceededException;
import com.insiders.poc1.exception.MainAddressDeleteException;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.repository.AddressRepository;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerService customerService;
    private final ModelMapper mapper;

    @Transactional
    public AddressResponseDto save(AddressRequestDto addressRequestDto) {
        Customer customer = mapper.map(customerService.findById(addressRequestDto.getCustomerRef()), Customer.class);
        Address address = mapper.map(addressRequestDto, Address.class);

        if (customer.getAddressList().isEmpty()) {
            address.setMainAddress(true);
        }
        if (customer.getAddressList().size() < 5) {
            address.setCustomer(customer);
            return mapper.map(addressRepository.save(address), AddressResponseDto.class);
        } else {
            throw new AddressLimitExceededException("Cannot add an address, this customer's address limit has been exceeded!");
        }
    }

    @Transactional
    public AddressResponseDto update(AddressRequestDto addressRequestDto, Long id) {
        Address address = mapper.map(findById(id), Address.class);
        address.setState(addressRequestDto.getState());
        address.setCity(addressRequestDto.getCity());
        address.setDistrict(addressRequestDto.getDistrict());
        address.setStreet(addressRequestDto.getStreet());
        address.setHouseNumber(addressRequestDto.getHouseNumber());
        address.setZipCode(addressRequestDto.getZipCode());
        return mapper.map(addressRepository.save(address), AddressResponseDto.class);
    }

    public AddressResponseDto findById(Long id) {
        return mapper.map(addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found!")),
                AddressResponseDto.class);
    }

    @Transactional
    public void updateMainAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found!"));

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
        Address address = mapper.map(findById(id), Address.class);
        if(address.isMainAddress()) {
            throw new MainAddressDeleteException("The main address can't be deleted!");
        }
        addressRepository.delete(address);
    }
}