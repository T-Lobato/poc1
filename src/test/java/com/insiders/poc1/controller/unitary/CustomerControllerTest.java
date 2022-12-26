package com.insiders.poc1.controller.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.insiders.poc1.controller.CustomerController;
import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private CustomerController customerController;

    private CustomerService customerService;

    private ModelMapper mapper;

    @BeforeEach
    public void setup() {
        customerService = mock(CustomerService.class);
        mapper = new ModelMapper();
        customerController = new CustomerController(mapper, customerService);
    }

    @Test
    @DisplayName("Must successfully receive a customerRequest and return a CustomerResponse")
    public void testSave() {
        // Cria um DTO de solicitação com os valores desejados
        CustomerRequestDto customerRequestDto = CustomerRequestDto.builder()
                .name("Thyago")
                .document("12633821774")
                .personType(PersonType.PF)
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .build();

        // Cria uma instância de Customer usando o DTO de solicitação
        Customer customer = mapper.map(customerRequestDto, Customer.class);

        // Configura o mock do serviço para retornar a instância de Customer quando o método "save()" for chamado
        when(customerService.save(customerRequestDto)).thenReturn(customer);

        // Chama o método "save()" da controller
        CustomerResponseDto customerResponseDto = customerController.save(customerRequestDto);

        // Verifica se o método "save()" do serviço foi chamado corretamente
        verify(customerService).save(customerRequestDto);

        // Verifica se o método "save()" da controller retornou o DTO de resposta esperado
        assertEquals(customer.getId(), customerResponseDto.getId());
        assertEquals(customer.getName(), customerResponseDto.getName());
        assertEquals(customer.getEmail(), customerResponseDto.getEmail());
        assertEquals(customer.getAddressList(), customerResponseDto.getAddressList());
    }
}