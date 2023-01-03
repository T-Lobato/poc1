package com.insiders.poc1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.controller.dto.request.AddressRequestUpdateDto;
import com.insiders.poc1.controller.dto.response.AddressResponseDto;
import com.insiders.poc1.entities.Address;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    private AddressController addressController;

    private AddressService addressService;

    private Customer customer;

    private ModelMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ModelMapper();
        addressService = mock(AddressService.class);
        addressController = new AddressController(mapper, addressService);
        customer = new Customer();
    }

    private Address addressCreate(){
       return Address.builder()
                .id(1L)
                .customer(customer)
                .state("São Paulo")
                .city("Sorocaba")
                .district("Vila Augusta")
                .street("Rua Ana Augusta")
                .houseNumber("470")
                .complement("Bloco 03 Apto 202")
                .zipCode("18040040")
                .mainAddress(true)
                .version(1L)
                .build();
    }

    @Test
    @DisplayName("Must successfully receive an AddressRequestDto and return an AddressResponseDto")
    void testSave(){

        // Cria um DTO de solicitação com os valores desejados
        AddressRequestDto addressRequestDto = AddressRequestDto.builder()
                .customerRef(1L)
                .uf("São Paulo")
                .localidade("Sorocaba")
                .bairro("Vila Augusta")
                .logradouro("Rua Ana Augusto")
                .houseNumber("470")
                .complement("Bloco 03 Apto 202")
                .cep("18040040")
                .build();

        // Cria uma instância de Address usando o DTO de solicitação
        Address address = Address.builder()
                .id(1L)
                .customer(customer)
                .state(addressRequestDto.getUf())
                .city(addressRequestDto.getLocalidade())
                .district(addressRequestDto.getBairro())
                .street(addressRequestDto.getLogradouro())
                .houseNumber(addressRequestDto.getHouseNumber())
                .complement(addressRequestDto.getComplement())
                .zipCode(addressRequestDto.getCep())
                .mainAddress(true)
                .version(1L)
                .build();

        // Configura o mock da service para retornar a instância de Address quando o método "save()" for chamado
        doReturn(address).when(addressService).save(addressRequestDto);

        // Chama o método "save()" da controller
        AddressResponseDto addressResponseDto = addressController.save(addressRequestDto);

        // Verifica se o método "save()" da service foi chamado corretamente
        verify(addressService).save(addressRequestDto);

        // Verifica se o método "save()" da controller retornou o DTO de resposta esperado
        assertEquals(address.getId(), addressResponseDto.getId());
        assertEquals(address.getState(), addressResponseDto.getState());
        assertEquals(address.getCity(), addressResponseDto.getCity());
        assertEquals(address.getDistrict(), addressResponseDto.getDistrict());
        assertEquals(address.getStreet(), addressResponseDto.getStreet());
        assertEquals(address.getHouseNumber(), addressResponseDto.getNumber());
        assertEquals(address.getComplement(), addressResponseDto.getComplement());
        assertEquals(address.getZipCode(), addressResponseDto.getZipCode());
        assertEquals(address.isMainAddress(), addressResponseDto.isMainAddress());
        assertEquals(address.getVersion(), addressResponseDto.getVersion());
    }

    @Test
    @DisplayName("Must successfully receive an id and return an AddressResponseDto")
    void testFindById() {
        // Cria um id
        Long id = 1L;

        // Configura o mock do AddressService para retornar o Address quando o método findById() for chamado
        when(addressService.findById(id)).thenReturn(addressCreate());

        // Chama o método findById() da AddressController
        AddressResponseDto addressResponseDto = addressController.findById(id);

        // Verifica se o método findById() do mock do AddressService foi chamado corretamente
        verify(addressService).findById(id);

        // Verifica se o método findById() da AddressController retornou o AddressResponseDto esperado
        assertEquals(addressCreate().getId(), addressResponseDto.getId());
        assertEquals(addressCreate().getState(), addressResponseDto.getState());
        assertEquals(addressCreate().getCity(), addressResponseDto.getCity());
        assertEquals(addressCreate().getDistrict(), addressResponseDto.getDistrict());
        assertEquals(addressCreate().getStreet(), addressResponseDto.getStreet());
        assertEquals(addressCreate().getHouseNumber(), addressResponseDto.getNumber());
        assertEquals(addressCreate().getComplement(), addressResponseDto.getComplement());
        assertEquals(addressCreate().getZipCode(), addressResponseDto.getZipCode());
        assertEquals(addressCreate().isMainAddress(), addressResponseDto.isMainAddress());
        assertEquals(addressCreate().getVersion(), addressResponseDto.getVersion());
    }

    @Test
    @DisplayName("Must successfully update an address")
    void testUpdate(){
        // Define o valor do id
        Long id = 1L;

        // Cria um DTO de solicitação com os valores desejados
        AddressRequestUpdateDto addressRequestUpdateDto = AddressRequestUpdateDto.builder()
                .houseNumber("470")
                .complement("Bloco 03 Apto 202")
                .cep("18040040")
                .build();

        // Cria uma instância de Address usando o DTO de solicitação
        Address address = Address.builder()
                .houseNumber(addressRequestUpdateDto.getHouseNumber())
                .complement(addressRequestUpdateDto.getComplement())
                .zipCode(addressRequestUpdateDto.getCep())
                .build();

        // Configura o mock da service para retornar a instância de Address quando o método "update()" for chamado
        when(addressService.update(addressRequestUpdateDto, id)).thenReturn(address);

        // Chama o método update da classe AddressController
        AddressResponseDto addressResponseDto = addressController.update(id, addressRequestUpdateDto);

        // Verifica se o resultado é igual ao esperado
        assertEquals(address.getHouseNumber(), addressResponseDto.getNumber());
        assertEquals(address.getComplement(), addressResponseDto.getComplement());
        assertEquals(address.getZipCode(), addressResponseDto.getZipCode());
    }

    @Test
    @DisplayName("Deve atualizar um endereço com sucesso e retornar o endereço atualizado")
    void testUpdateMainAddress() {
        // Configura o mock do addressService.updateMainAddress() para não fazer nada quando chamado com o argumento 1L
        doNothing().when(addressService).updateMainAddress(1L);

        // Configura o mock do addressService.findById() para retornar o objeto Address quando chamado com o argumento 1L
        when(addressService.findById(1L)).thenReturn(addressCreate());

        // Chama o método updateMainAddress() da controller
        AddressResponseDto addressResponseDto = addressController.updateMainAddress(1L);

        // Verifica se o método updateMainAddress() do mock addressService foi chamado com o argumento correto
        verify(addressService).updateMainAddress(1L);

        // Verifica se o AddressResponseDto retornado tem os valores corretos
        assertEquals(addressCreate().getId(), addressResponseDto.getId());
        assertEquals(addressCreate().getState(), addressResponseDto.getState());
        assertEquals(addressCreate().getCity(), addressResponseDto.getCity());
        assertEquals(addressCreate().getDistrict(), addressResponseDto.getDistrict());
        assertEquals(addressCreate().getStreet(), addressResponseDto.getStreet());
        assertEquals(addressCreate().getHouseNumber(), addressResponseDto.getNumber());
        assertEquals(addressCreate().getComplement(), addressResponseDto.getComplement());
        assertEquals(addressCreate().getZipCode(), addressResponseDto.getZipCode());
        // ...
    }

    @Test
    @DisplayName("Must successfully delete an address")
    void testDeleteById() {
        // Define um id
        Long id = 1L;

        // Chama o método deleteById da classe AddressController
        addressController.deleteById(id);

        // Verifica se o método deleteById foi chamado
        verify(addressService).deleteById(id);
    }
}