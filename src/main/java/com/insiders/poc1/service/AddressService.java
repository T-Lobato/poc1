package com.insiders.poc1.service;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.request.AddressRequestUpdateDto;
import com.insiders.poc1.entities.Address;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.exception.AddressLimitExceededException;
import com.insiders.poc1.exception.MainAddressDeleteException;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.integrations.ViaCepApi;
import com.insiders.poc1.repository.AddressRepository;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerService customerService;
    private final ViaCepApi viaCepApi;

    @Transactional
    public Address save(AddressRequestDto addressRequestDto){

        AddressRequestDto addressAux = viaCepApi.getCompleteAddress(addressRequestDto.getCep());
        Address address = new Address();
        address.setZipCode(addressRequestDto.getCep());
        address.setState(addressAux.getUf());
        address.setCity(addressAux.getLocalidade());
        address.setDistrict(addressAux.getBairro());
        address.setStreet(addressAux.getLogradouro());
        address.setHouseNumber(addressRequestDto.getHouseNumber());
        address.setComplement(addressRequestDto.getComplement());

        Customer customer = customerService.findById(addressRequestDto.getCustomerRef());
        address.setCustomer(customer);

        setFirstAddressToMain(address);
        verifyCustomerAddressListSizeLimit(customer);

        return addressRepository.save(address);
    }

    @Transactional
    public Address update(AddressRequestUpdateDto addressRequestUpdateDto, Long id) {
        Address address = this.findById(id);
        AddressRequestDto addressAux = viaCepApi.getCompleteAddress(addressRequestUpdateDto.getCep());
        address.setZipCode(addressRequestUpdateDto.getCep());
        address.setState(addressAux.getUf());
        address.setCity(addressAux.getLocalidade());
        address.setDistrict(addressAux.getBairro());
        address.setStreet(addressAux.getLogradouro());
        address.setComplement(addressRequestUpdateDto.getComplement());
        address.setHouseNumber(addressRequestUpdateDto.getHouseNumber());

        addressRepository.save(address);
        return address;
    }

    public Address findById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found!"));
    }

    @Transactional
    public void updateMainAddress(Long id) {
        Address address = this.findById(id);
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
        Address address = this.findById(id);
        if(address.isMainAddress()) {
            throw new MainAddressDeleteException("The main address can't be deleted!");
        }
        addressRepository.delete(address);
    }

    public void setFirstAddressToMain(Address address){
        if (address.getCustomer().getAddressList().isEmpty()) {
            address.setMainAddress(true);
        }
    }

    public void verifyCustomerAddressListSizeLimit(Customer customer){
        if (customer.getAddressList().size() > 4)
            throw new AddressLimitExceededException("Cannot add an address, this customer's address limit has been exceeded!");
    }
}