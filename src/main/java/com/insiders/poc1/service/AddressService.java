package com.insiders.poc1.service;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.request.AddressRequestUpdateDto;
import com.insiders.poc1.controller.dto.response.AddressResponseDto;
import com.insiders.poc1.entities.Address;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.exception.AddressLimitExceededException;
import com.insiders.poc1.exception.MainAddressDeleteException;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.integrations.ViaCepApi;
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
    private final ViaCepApi viaCepApi;

    @Transactional
    public AddressResponseDto save(AddressRequestDto addressRequestDto){

        AddressRequestDto addressAux = viaCepApi.getCompleteAddress(addressRequestDto.getCep());
        Address address = new Address();
        address.setZipCode(addressRequestDto.getCep());
        address.setState(addressAux.getUf());
        address.setCity(addressAux.getLocalidade());
        address.setDistrict(addressAux.getBairro());
        address.setStreet(addressAux.getLogradouro());
        address.setHouseNumber(addressRequestDto.getHouseNumber());
        address.setComplement(addressRequestDto.getComplement());

        Customer customer = mapper.map(customerService.findById(addressRequestDto.getCustomerRef()), Customer.class);
        address.setCustomer(customer);

        setFirstAddressToMain(address);
        verifyCustomerAddressListSizeLimit(customer);

        return mapper.map(addressRepository.save(address), AddressResponseDto.class);
    }

    @Transactional
    public AddressResponseDto update(AddressRequestUpdateDto addressRequestUpdateDto, Long id) {
        Address address = mapper.map(addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found!")),
        Address.class);
        AddressRequestDto addressAux = viaCepApi.getCompleteAddress(addressRequestUpdateDto.getCep());
        address.setZipCode(addressRequestUpdateDto.getCep());
        address.setState(addressAux.getUf());
        address.setCity(addressAux.getLocalidade());
        address.setDistrict(addressAux.getBairro());
        address.setStreet(addressAux.getLogradouro());
        address.setComplement(addressRequestUpdateDto.getComplement());
        address.setHouseNumber(addressRequestUpdateDto.getHouseNumber());

        addressRepository.save(address);
        return mapper.map(address, AddressResponseDto.class);
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

    private void setFirstAddressToMain(Address address){
        if (address.getCustomer().getAddressList().isEmpty()) {
            address.setMainAddress(true);
        }
    }

    private void verifyCustomerAddressListSizeLimit(Customer customer){
        if (customer.getAddressList().size() > 4)
            throw new AddressLimitExceededException("Cannot add an address, this customer's address limit has been exceeded!");
    }
}