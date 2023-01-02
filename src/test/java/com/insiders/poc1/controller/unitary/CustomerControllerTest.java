package com.insiders.poc1.controller.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.insiders.poc1.controller.CustomerController;
import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseV2Dto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.service.CustomerService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;


@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private CustomerController customerController;

    private CustomerService customerService;

    private ModelMapper mapper;

    @BeforeEach
    void setup() {
        customerService = mock(CustomerService.class);
        mapper = new ModelMapper();
        customerController = new CustomerController(mapper, customerService);
    }

    @Test
    @DisplayName("Must successfully receive a customerRequest and return a CustomerResponse")
    void testSave() {
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

    @Test
    @DisplayName("Must successfully receive an id and return a CustomerResponse")
    void testFindById() {
        // Cria um id
        Long id = 1L;
        // Cria um Customer
        Customer customer = Customer.builder()
                .id(id)
                .name("Thyago")
                .document("12633821774")
                .personType(PersonType.PF)
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .build();
        // Configura o mock do CustomerService para retornar o Customer quando o método findById() for chamado com o id
        when(customerService.findById(id)).thenReturn(customer);

        // Chama o método findById() da CustomerController
        CustomerResponseDto customerResponseDto = customerController.findById(id);

        // Verifica se o método findById() do mock do CustomerService foi chamado corretamente
        verify(customerService).findById(id);

        // Verifica se o método findById() da CustomerController retornou o CustomerResponseDto esperado
        assertEquals(customer.getId(), customerResponseDto.getId());
        assertEquals(customer.getName(), customerResponseDto.getName());
        assertEquals(customer.getEmail(), customerResponseDto.getEmail());
        assertEquals(customer.getAddressList(), customerResponseDto.getAddressList());
    }

    @Test
    @DisplayName("Must return 404 when id does not exist")
    void testFindByIdNotFound() {
        // Cria um id
        Long id = 1L;
        // Configura o mock do CustomerService para lançar uma exceção do tipo ResourceNotFoundException quando o método findById() for chamado com o id
        when(customerService.findById(id)).thenThrow(new ResourceNotFoundException("Customer Not Found!"));

        // Verifica se o método findById() da CustomerController lança uma exceção do tipo ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> customerController.findById(id));
    }

    @Test
    @DisplayName("Must successfully receive an id and return a CustomerResponseV2")
    void testFindByIdV2() {
        // Cria um id
        Long id = 1L;
        // Cria um customer
        Customer customer = Customer.builder()
                .id(id)
                .name("Thyago")
                .document("12633821774")
                .personType(PersonType.PF)
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .build();
        // Configura o mock do CustomerService para retornar o Customer quando o método findByIdV2() for chamado com o id
        when(customerService.findById(id)).thenReturn(customer);

        // Chama o método findByIdV2() da CustomerController
        CustomerResponseV2Dto customerResponseV2Dto = customerController.findByIdV2(id);

        // Verifica se o método findById() do mock do CustomerService foi chamado corretamente
        verify(customerService).findById(id);

        // Verifica se o método findByIdV2() da CustomerController retornou o CustomerResponseDto esperado
        assertEquals(customer.getId(), customerResponseV2Dto.getId());
        assertEquals(customer.getName(), customerResponseV2Dto.getName());
        assertEquals(customer.getEmail(), customerResponseV2Dto.getEmail());
        assertEquals(customer.getAddressList(), customerResponseV2Dto.getAddressList());
        assertEquals(customer.getPersonType(), customerResponseV2Dto.getPersonType());
        assertEquals(customer.getPhoneNumber(), customerResponseV2Dto.getPhoneNumber());
    }

    @Test
    @DisplayName("Must successfully return a page of customers")
    void testFindAll() {
        // Cria uma instância de Pageable
        Pageable pageable = PageRequest.of(0, 3, Sort.by("name").ascending());
        // Cria uma lista de customers
        List<Customer> customers = Arrays.asList(
                Customer.builder().id(1L).name("Thyago").build(),
                Customer.builder().id(2L).name("Lucas").build(),
                Customer.builder().id(3L).name("Mauricio").build()
        );
        // Configura o mock do CustomerService para retornar uma Page de customers quando o método findAll() for chamado com o Pageable
        when(customerService.findAll(pageable)).thenReturn(new PageImpl<>(customers, pageable, customers.size()));

        // Chama o método findAll() da controller
        Page<CustomerResponseDto> customerResponseDtos = customerController.findAll(pageable, null);

        // Verifica se o método findAll() do serviço foi chamado corretamente
        verify(customerService).findAll(pageable);

        // Verifica se o método findAll() da controller retornou a Page de DTOs de resposta esperada
        assertEquals(customers.size(), customerResponseDtos.getTotalElements());
        assertEquals(customers.get(0).getId(), customerResponseDtos.getContent().get(0).getId());
        assertEquals(customers.get(1).getId(), customerResponseDtos.getContent().get(1).getId());
        assertEquals(customers.get(2).getId(), customerResponseDtos.getContent().get(2).getId());
    }

    @Test
    @DisplayName("Must successfully return a page of customers filtered by name")
    void testFindCustomerByName() {
        // Cria uma instância de Pageable
        Pageable pageable = PageRequest.of(0, 3, Sort.by("name").ascending());
        // Cria uma lista de customers
        List<Customer> customers = Arrays.asList(
                Customer.builder().id(1L).name("Thyago").build(),
                Customer.builder().id(2L).name("Thyago").build(),
                Customer.builder().id(3L).name("Thyago").build()
        );
        // Configura o mock do CustomerService para retornar uma Page de customers quando o método findByName() for chamado com o nome e o Pageable
        when(customerService.findByName("Thyago", pageable)).thenReturn(new PageImpl<>(customers, pageable, customers.size()));

        // Chama o método findCustomerByName() da controller
        Page<CustomerResponseDto> customerResponseDtos = customerController.findCustomerByName("Thyago", pageable);

        // Verifica se o método findByName() do serviço foi chamado corretamente
        verify(customerService).findByName("Thyago", pageable);

        // Verifica se o método findCustomerByName() da controller retornou a Page de DTOs de resposta esperada
        assertEquals(customers.size(), customerResponseDtos.getTotalElements());
        assertEquals(customers.get(0).getId(), customerResponseDtos.getContent().get(0).getId());
        assertEquals(customers.get(1).getId(), customerResponseDtos.getContent().get(1).getId());
        assertEquals(customers.get(2).getId(), customerResponseDtos.getContent().get(2).getId());
    }

    @Test
    @DisplayName("Must successfully delete a customer")
    void testDeleteById() {
        // Define o valor do parâmetro "id"
        Long id = 1L;

        // Chama o método deleteById da classe CustomerController
        customerController.deleteById(id);

        // Verifica se o método deleteById foi chamado
        verify(customerService).deleteById(id);
    }

    @Test
    @DisplayName("Must successfully update a customer")
    void testUpdate() {
        // Define o valor do parâmetro "id"
        Long id = 1L;

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

        // Configura o mock do serviço para retornar a instância de Customer quando o método "update()" for chamado
        when(customerService.update(customerRequestDto, id)).thenReturn(customer);

        // Chama o método update da classe CustomerController
        CustomerResponseDto result = customerController.update(id, customerRequestDto);

        // Verifica se o resultado é igual ao esperado
        assertEquals(customer.getId(), result.getId());
        assertEquals(customer.getName(), result.getName());
    }
}