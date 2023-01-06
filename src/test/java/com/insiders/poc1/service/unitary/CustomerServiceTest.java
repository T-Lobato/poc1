package com.insiders.poc1.service.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.repository.CustomerRepository;
import com.insiders.poc1.service.CustomerService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerRepository customerRepository;

    private CustomerService customerService;

    private ModelMapper mapper;


    @BeforeEach
    void setup() {
        mapper = new ModelMapper();
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository, mapper);
    }

    private CustomerRequestDto customerRequestCreate() {
        return CustomerRequestDto.builder()
                .name("Thyago")
                .document("12633821774")
                .personType(PersonType.PF)
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .build();
    }

    private Customer customerCreate() {
        return Customer.builder()
                .name("Thyago")
                .document("12633821774")
                .personType(PersonType.PF)
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .build();
    }

    @Test
    @DisplayName("Must successfully receive a customerRequestDto and save a Customer")
    void testSave() {

        // Cria uma instância de Customer usando o DTO de solicitação
        Customer customer = mapper.map(customerRequestCreate(), Customer.class);

        // Configura o mock do repository para retornar a instância de Customer quando o método "save()" for chamado
        doReturn(customer).when(customerRepository).save(any());

        // Chama o método "save()" da service
        customer = customerService.save(customerRequestCreate());

        // Verifica se o método "save()" do repository foi chamado corretamente
        verify(customerRepository).save(any());

        // Verifica se o método "save()" da service retornou o Customer esperado
        assertEquals(customerRequestCreate().getName(), customer.getName());
        assertEquals(customerRequestCreate().getDocument(), customer.getDocument());
        assertEquals(customerRequestCreate().getPersonType(), customer.getPersonType());
        assertEquals(customerRequestCreate().getEmail(), customer.getEmail());
        assertEquals(customerRequestCreate().getPhoneNumber(), customer.getPhoneNumber());
    }

    @Test
    @DisplayName("Must successfully receive an id and return a Customer")
    void testFindById() {
        // Cria um id
        Long id = 1L;

        // Configura o mock do CustomerRepository para retornar o Customer quando o método findById() for chamado com o id
        when(customerRepository.findById(id)).thenReturn(Optional.of(customerCreate()));


        // Chama o método findById() da CustomerService
        Customer customer = customerService.findById(id);

        // Verifica se o método findById() do mock do customerRepository foi chamado corretamente
        verify(customerRepository).findById(id);

        // Verifica se o método findById() da CustomerService retornou o Customer esperado
        assertEquals(customer.getName(), customerCreate().getName());
        assertEquals(customer.getDocument(), customerCreate().getDocument());
        assertEquals(customer.getPersonType(), customerCreate().getPersonType());
        assertEquals(customer.getEmail(), customerCreate().getEmail());
        assertEquals(customer.getPhoneNumber(), customerCreate().getPhoneNumber());

    }

    @Test
    @DisplayName("Must throw a ResourceNotFoundException when a Customer is not found")
    void testFindById_notFound() {
        // Cria um id
        Long id = 1L;

        // Configura o mock do CustomerRepository para retornar um objeto Optional vazio
        // quando o método findById() é chamado
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Espera que a exceção ResourceNotFoundException seja lançada pelo método findById()
        assertThrows(ResourceNotFoundException.class, () -> customerService.findById(id));
    }

    @Test
    @DisplayName("Must return a page of Customers")
    void testFindAll() {
        // Cria uma instância de Pageable
        Pageable pageable = PageRequest.of(0, 10);

        // Cria uma lista de Customers
        List<Customer> customers = Arrays.asList(customerCreate(), customerCreate(), customerCreate());

        // Cria uma instância de Page com a lista de Customers
        Page<Customer> page = new PageImpl<>(customers);

        // Configura o mock do CustomerRepository para retornar a Page quando o método findAll() é chamado
        when(customerRepository.findAll(pageable)).thenReturn(page);

        // Chama o método findAll() da CustomerService
        Page<Customer> result = customerService.findAll(pageable);

        // Verifica se o método findAll() do mock do CustomerRepository foi chamado corretamente
        verify(customerRepository).findAll(pageable);

        // Verifica se o método findAll() da CustomerService retornou a Page esperada
        assertEquals(page, result);
    }

    @Test
    @DisplayName("Must successfully return a page of customers filtered by name")
    void testFindCustomerByName() {
        // Cria uma instância de Pageable
        Pageable pageable = PageRequest.of(0, 3, Sort.by("name").ascending());

        // Cria uma lista de customers
        List<Customer> customers = Arrays.asList(customerCreate(), customerCreate(), customerCreate());

        // Configura o mock do CustomerRepository para retornar uma Page de customers
        // quando o método findByName() for chamado com o nome e o Pageable
        when(customerRepository.findByNameContainsIgnoreCase("Thyago", pageable))
                .thenReturn(new PageImpl<>(customers, pageable, customers.size()));

        // Chama o método findByName da CustomerService
        Page<Customer> customerResult = customerService.findByName("Thyago", pageable);

        // Verifica se o método findByNameContainsIgnoreCase()
        // do mock do CustomerRepository foi chamado corretamente
        verify(customerRepository).findByNameContainsIgnoreCase("Thyago", pageable);

        // Verifica se o método findByName() da service retornou a Page Customers esperada
        assertEquals(customers.size(), customerResult.getTotalElements());
        assertEquals(pageable.getPageNumber(), customerResult.getPageable().getPageNumber());
        assertEquals(pageable.getPageSize(), customerResult.getPageable().getPageSize());
        assertEquals(customers, customerResult.getContent());
    }

    @Test
    @DisplayName("Must successfully delete a customer")
    void testDeleteById() {
        // Define o valor do parâmetro "id"
        Long id = 1L;

        // Configura o mock do CustomerRepository para retornar o Customer quando o método findById() for chamado
        when(customerRepository.findById(id)).thenReturn(Optional.of(customerCreate()));

        // Chama o método deleteById da classe CustomerService
        customerService.deleteById(id);

        // Verifica se o método delete() do CustomerRepository foi chamado
        verify(customerRepository).delete(any(Customer.class));
    }

    @Test
    @DisplayName("Must successfully update a customer")
    void testUpdate() {
        // Cria uma instância de CustomerRequestDto com os novos dados do customer
        CustomerRequestDto customerRequestDto = customerRequestCreate();
        customerRequestDto.setName("Thyago Atualizado");

        // Define o valor do parâmetro "id"
        Long id = 1L;

        // Cria uma instância de Customer com os dados antigos do customer
        Customer customerOld = customerCreate();

        // Configura o mock do CustomerRepository para retornar o Customer quando o método findById() for chamado
        when(customerRepository.findById(id)).thenReturn(Optional.of(customerOld));

        // Chama o método update da classe CustomerService
        Customer customer = customerService.update(customerRequestDto, id);

        // Verifica se o método save() do CustomerRepository foi chamado corretamente com o Customer atualizado
        verify(customerRepository).save(customer);

        // Verifica se o método update() da CustomerService retornou o Customer atualizado corretamente
        assertEquals(customerRequestDto.getName(), customer.getName());
        assertEquals(customerOld.getDocument(), customer.getDocument());
        assertEquals(customerOld.getPersonType(), customer.getPersonType());
        assertEquals(customerOld.getEmail(), customer.getEmail());
        assertEquals(customerOld.getPhoneNumber(), customer.getPhoneNumber());
    }

}