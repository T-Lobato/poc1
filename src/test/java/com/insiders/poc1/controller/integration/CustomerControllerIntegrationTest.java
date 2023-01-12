package com.insiders.poc1.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.insiders.poc1.controller.CustomerController;
import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseV2Dto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.repository.CustomerRepository;
import com.insiders.poc1.service.CustomerService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CustomerControllerIntegrationTest {

    private final long ID = 1L;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer createCustomer() {
        return Customer.builder()
                .id(ID)
                .name("Thyago")
                .personType(PersonType.PF)
                .document("12633821774")
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .addressList(new ArrayList<>())
                .build();
    }

    private CustomerRequestDto createCustomerRequestDto() {
        return CustomerRequestDto.builder()
                .name("Thyago")
                .document("12633821774")
                .personType(PersonType.PF)
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .build();
    }

    @BeforeEach
    void setup() {
        ModelMapper mapper = new ModelMapper();
        CustomerService customerService = new CustomerService(customerRepository, mapper);
        customerController = new CustomerController(mapper, customerService);
    }

    @Test
    @DisplayName("Must successfully receive a customerRequest and return a CustomerResponse")
    void testSave() {
        // Cria um DTO de solicitação com os valores desejados
        CustomerRequestDto customerRequestDto = createCustomerRequestDto();

        // Chama o método "save()" da controller
        CustomerResponseDto customerResponseDto = customerController.save(customerRequestDto);

        // Verifica se o método "save()" da controller retornou o DTO de resposta esperado
        assertNotNull(customerResponseDto.getId());
        assertEquals(customerRequestDto.getName(), customerResponseDto.getName());
        assertEquals(customerRequestDto.getEmail(), customerResponseDto.getEmail());

        // Verifica se o customer foi realmente salvo no banco de dados
        Customer savedCustomer = customerRepository.findById(customerResponseDto.getId()).orElse(null);
        assertNotNull(savedCustomer);
        assertEquals(customerRequestDto.getName(), savedCustomer.getName());
        assertEquals(customerRequestDto.getDocument(), savedCustomer.getDocument());
        assertEquals(customerRequestDto.getPersonType(), savedCustomer.getPersonType());
        assertEquals(customerRequestDto.getEmail(), savedCustomer.getEmail());
        assertEquals(customerRequestDto.getPhoneNumber(), savedCustomer.getPhoneNumber());
    }

    @Test
    @DisplayName("Must successfully find a customer by id")
    void testFindById() {

        // Cria um customer
        Customer customer = createCustomer();

        // Salva o customer criado no banco de dados
        customerRepository.save(customer);

        // Chama o método "findById()" da controller passando o id do customer salvo
        CustomerResponseDto customerResponseDto = customerController.findById(customer.getId());

        // Verifica se o método "findById()" da controller retornou o DTO de resposta esperado
        assertNotNull(customerResponseDto);
        assertEquals(customer.getId(), customerResponseDto.getId());
        assertEquals(customer.getName(), customerResponseDto.getName());
        assertEquals(customer.getEmail(), customerResponseDto.getEmail());
        assertIterableEquals(customer.getAddressList(), customerResponseDto.getAddressList());
    }

    @Test
    @DisplayName("Must successfully find a customer by id")
    void testFindByIdV2() {

        // Cria um customer
        Customer customer = createCustomer();

        // Salva o customer criado no banco de dados
        customerRepository.save(customer);

        // Chama o método "findById()" da controller passando o id do customer salvo
        CustomerResponseV2Dto customerResponseV2Dto = customerController.findByIdV2(customer.getId());

        // Verifica se o método "findById()" da controller retornou o DTO de resposta esperado
        assertNotNull(customerResponseV2Dto);
        assertEquals(customer.getId(), customerResponseV2Dto.getId());
        assertEquals(customer.getName(), customerResponseV2Dto.getName());
        assertEquals(customer.getEmail(), customerResponseV2Dto.getEmail());
        assertEquals(customer.getPersonType(), customerResponseV2Dto.getPersonType());
        assertEquals(customer.getPhoneNumber(), customerResponseV2Dto.getPhoneNumber());
        assertIterableEquals(customer.getAddressList(), customerResponseV2Dto.getAddressList());
    }
}