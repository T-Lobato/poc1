package com.insiders.poc1.controller.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.insiders.poc1.controller.CustomerController;
import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseV2Dto;
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

    @Test
    @DisplayName("Must successfully receive an id and return a CustomerResponse")
    public void testFindById() {
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
    @DisplayName("Must successfully receive an id and return a CustomerResponseV2")
    public void testFindByIdV2(){
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
    public void testFindAll() {
        //TODO
    }

    @Test
    public void testFindCustomerByName() {
        // TODO
    }

    @Test
    @DisplayName("Must successfully delete a customer")
    public void testDeleteById() {
        // Define o valor do parâmetro "id"
        Long id = 1L;

        // Chama o método deleteById da classe CustomerController
        customerController.deleteById(id);

        // Verifica se o método deleteById foi chamado
        verify(customerService).deleteById(id);
    }

    @Test
    @DisplayName("Must successfully update a customer")
    public void testUpdate() {
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