package com.insiders.poc1.service;

import com.insiders.poc1.entities.Address;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.repository.AddressCustomRepository;
import com.insiders.poc1.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerService customerService;
    private final AddressCustomRepository addressCustomRepository;

    @Transactional
    public Address save(Address address, Long customerId) {
        Customer customer = customerService.findById(customerId);
        if(customer.getAddressList().size() < 5) {
            address.setCustomer(customer);
            return addressRepository.save(address);
        } else {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"cannot add an address, this customer's address limit has been exceeded");
        }
    }

    @Transactional
    public void updateAllMainAddressToFalse(Long customerId){
       addressCustomRepository.updateAllMainAdressToFalse(customerId);
    }

    public Address findById(Long id){
        return addressRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not Found!"));
    }
}