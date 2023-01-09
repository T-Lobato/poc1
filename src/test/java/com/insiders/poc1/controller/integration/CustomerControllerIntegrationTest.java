package com.insiders.poc1.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.insiders.poc1.controller.CustomerController;
import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.repository.CustomerRepository;
import com.insiders.poc1.service.CustomerService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CustomerControllerIntegrationTest {

    private final long ID = 1L;

    private ModelMapper mapper;

    private CustomerController customerController;

    private CustomerService customerService;

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerControllerIntegrationTest(
            CustomerRepository customerRepository, ModelMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @BeforeEach
    void setup() {
        customerService = new CustomerService(customerRepository, mapper);
        customerController = new CustomerController(mapper, customerService);
    }

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
        Customer customer = customerRepository.findById(customerResponseDto.getId()).orElse(null);
        assertNotNull(customer);
        assertEquals(customerRequestDto.getName(), customer.getName());
        assertEquals(customerRequestDto.getDocument(), customer.getDocument());
        assertEquals(customerRequestDto.getPersonType(), customer.getPersonType());
        assertEquals(customerRequestDto.getEmail(), customer.getEmail());
        assertEquals(customerRequestDto.getPhoneNumber(), customer.getPhoneNumber());
    }
}