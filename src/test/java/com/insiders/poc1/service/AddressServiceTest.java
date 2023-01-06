package com.insiders.poc1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.request.AddressRequestUpdateDto;
import com.insiders.poc1.entities.Address;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.exception.AddressLimitExceededException;
import com.insiders.poc1.exception.MainAddressDeleteException;
import com.insiders.poc1.exception.ResourceNotFoundException;
import com.insiders.poc1.integrations.ViaCepApi;
import com.insiders.poc1.repository.AddressRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    private static final Long ID = 1L;

    private static final String ZIP_CODE = "18040040";

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ViaCepApi viaCepApi;

    private AddressService addressService;


    @BeforeEach
    public void setUp() {
        addressService = new AddressService(addressRepository, customerService, viaCepApi);
    }

    private AddressRequestDto createAddressRequestDto() {
        return AddressRequestDto.builder().customerRef(ID).uf("São Paulo").localidade("Sorocaba").bairro("Vila Augusta").logradouro("Rua Ana Augusto")
                .complement("Bloco 03").houseNumber("470").cep(ZIP_CODE).build();
    }

    private Customer createCustomer() {
        return Customer.builder().id(ID).name("Thyago").personType(PersonType.PF).document("12633821774").email("thyagollobato@gmail.com")
                .phoneNumber("15981229370").addressList(new ArrayList<>()).build();
    }

    private Address createAddress() {
        return Address.builder().id(ID).customer(createCustomer()).state("São Paulo").city("Sorocaba").district("Vila Augusta")
                .street("Rua Ana Augusto").complement("Bloco 03").houseNumber("470").zipCode(ZIP_CODE).mainAddress(true).build();
    }

    private AddressRequestUpdateDto createAddressRequestUpdateDto() {
        return AddressRequestUpdateDto.builder().complement("Casa 01").houseNumber("325").cep("23015180").build();
    }

    private AddressRequestDto createAddressViaCep() {
        return AddressRequestDto.builder().uf("Rio de Janeiro").localidade("Rio de janeiro").bairro("Campo Grande")
                .logradouro("Rua União dos Palmares").build();
    }

    @Test
    @DisplayName("Must successfully receive an AddressRequestDto and save an Address")
    void testSave() {

        // Configura os mocks para retornar os valores esperados
        when(customerService.findById(ID)).thenReturn(createCustomer());
        when(viaCepApi.getCompleteAddress(ZIP_CODE)).thenReturn(createAddressRequestDto());
        when(addressRepository.save(any())).thenReturn(createAddress());

        // Executa o método a ser testado
        Address savedAddress = addressService.save(createAddressRequestDto());

        // Verifica se o resultado é o esperado
        assertEquals(savedAddress.getState(), createAddressRequestDto().getUf());
        assertEquals(savedAddress.getCity(), createAddressRequestDto().getLocalidade());
        assertEquals(savedAddress.getDistrict(), createAddressRequestDto().getBairro());
        assertEquals(savedAddress.getStreet(), createAddressRequestDto().getLogradouro());
        assertEquals(savedAddress.getHouseNumber(), createAddressRequestDto().getHouseNumber());
        assertEquals(savedAddress.getComplement(), createAddressRequestDto().getComplement());
        assertEquals(savedAddress.getZipCode(), createAddressRequestDto().getCep());
    }

    @Test
    @DisplayName("Must successfully update an Address")
    void testUpdate() {

        // Cria o mock do AddressRepository para retornar o Address quando o método findById() for chamado
        when(addressRepository.findById(ID)).thenReturn(Optional.of(createAddress()));

        // Cria o mock do ViaCepApi para retornar o AddressRequestDto
        // quando o método getCompleteAddress() for chamado
        when(viaCepApi.getCompleteAddress(createAddressRequestUpdateDto().getCep())).thenReturn(createAddressViaCep());

        // Chama o método update da classe AddressService
        Address savedAddress = addressService.update(createAddressRequestUpdateDto(), ID);

        // Verifica se o método save() do AddressRepository foi chamado
        verify(addressRepository).save(any(Address.class));

        // Verifica se o resultado é o esperado
        assertEquals(savedAddress.getState(), createAddressViaCep().getUf());
        assertEquals(savedAddress.getCity(), createAddressViaCep().getLocalidade());
        assertEquals(savedAddress.getDistrict(), createAddressViaCep().getBairro());
        assertEquals(savedAddress.getStreet(), createAddressViaCep().getLogradouro());
        assertEquals(savedAddress.getHouseNumber(), createAddressRequestUpdateDto().getHouseNumber());
        assertEquals(savedAddress.getComplement(), createAddressRequestUpdateDto().getComplement());
        assertEquals(savedAddress.getZipCode(), createAddressRequestUpdateDto().getCep());
    }

    @Test
    @DisplayName("Must successfully update an address to main")
    void testUpdateToMainAddress() {

        // Cria uma variável do tipo Address
        Address address = createAddress();

        // Configura o mock do AddressRepository para retornar o Address quando o método findById() for chamado
        when(addressRepository.findById(ID)).thenReturn(Optional.of(address));

        // Cria uma lista de Address e atribui ao objeto Customer retornado pelo mock do AddressRepository
        List<Address> addressList = Arrays.asList(createAddress(), createAddress(), createAddress());
        address.getCustomer().setAddressList(addressList);

        // Define o valor de mainAddress de cada um dos endereços da lista para false
        // exceto para o endereço que será atualizado
        addressList.forEach(n -> n.setMainAddress(false));
        address.setMainAddress(true);

        // Chama o método updateMainAddress da classe AddressService com o ID do endereço que será atualizado
        addressService.updateMainAddress(ID);

        // Verifica se o método save() do AddressRepository foi chamado para cada um dos endereços da lista
        addressList.forEach(n -> verify(addressRepository).save(n));

        // Verifica se o valor de mainAddress do endereço atualizado é igual a true
        assertTrue(address.isMainAddress());
    }

    @Test
    @DisplayName("Must successfully receive an id and return an Address")
    void testFindById() {

        // Configura o mock do CustomerRepository para retornar o Address
        // quando o método findById() for chamado
        when(addressRepository.findById(ID)).thenReturn((Optional.of(createAddress())));

        // Chama o método findById() da CustomerService
        Address address = addressService.findById(ID);

        // Verifica se o método findById() do mock do AddressService foi chamado corretamente
        verify(addressRepository).findById(ID);

        // Verifica se o método findById() da CustomerService retornou o Customer esperado
        assertEquals(address.getState(), createAddressRequestDto().getUf());
        assertEquals(address.getCity(), createAddressRequestDto().getLocalidade());
        assertEquals(address.getDistrict(), createAddressRequestDto().getBairro());
        assertEquals(address.getStreet(), createAddressRequestDto().getLogradouro());
        assertEquals(address.getHouseNumber(), createAddressRequestDto().getHouseNumber());
        assertEquals(address.getComplement(), createAddressRequestDto().getComplement());
        assertEquals(address.getZipCode(), createAddressRequestDto().getCep());
    }

    @Test
    @DisplayName("Must throw a ResourceNotFoundException when an Address is not found")
    void testFindById_notFound() {
        // Configura o mock do AddressRepository para retornar um objeto Optional vazio
        // quando o método findById() é chamado
        when(addressRepository.findById(ID)).thenReturn(Optional.empty());

        // Espera que a exceção ResourceNotFoundException seja lançada pelo método findById()
        assertThrows(ResourceNotFoundException.class, () -> addressService.findById(ID));
    }

    @Test
    @DisplayName("Must successfully delete an Address")
    void testDeleteById() {

        // Instancia um novo Address e muda o atributo mainAddress para false
        Address addressMainFalse = createAddress();
        addressMainFalse.setMainAddress(false);

        // Configura o mock do AddressRepository para retornar o Address quando o método findById() for chamado
        when(addressRepository.findById(ID)).thenReturn(Optional.of(addressMainFalse));

        // Chama o método deleteById da classe CustomerService
        addressService.deleteById(ID);

        // Verifica se o método delete() do CustomerRepository foi chamado
        verify(addressRepository).delete(any(Address.class));
    }

    @Test
    @DisplayName("Must throw a MainAddressDeleteException when it try to delete a Main Address")
    void testDeleteMainAddress() {

        // Configura o mock do AddressRepository para retornar o Address quando o método findById() for chamado
        when(addressRepository.findById(ID)).thenReturn(Optional.of(createAddress()));

        // Espera que a exceção ResourceNotFoundException seja lançada pelo método findById()
        assertThrows(MainAddressDeleteException.class, () -> addressService.deleteById(ID));
    }

    @Test
    @DisplayName("Must throw AddressLimitExceededException when customer has more than 4 addresses")
    void testCustomerAddressListLimit() {
        // Cria um Customer com mais de 4 endereços na lista
        Customer customer =
                Customer.builder().addressList(Arrays.asList(createAddress(), createAddress(), createAddress(), createAddress(), createAddress()))
                        .build();

        // Verifica se o método verifyCustomerAddressListSizeLimit lança a exceção esperada
        assertThrows(AddressLimitExceededException.class, () -> addressService.verifyCustomerAddressListSizeLimit(customer));
    }
}