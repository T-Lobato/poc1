package com.insiders.poc1.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.exception.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ViaCepApiTest {

    private ViaCepApi viaCepApi = new ViaCepApi();

    @Test
    @DisplayName("Must successfully receive an zip_code and return an address from viaCepApi")
    void testGetCompleteAddress() {
        // Arrange
        String cep = "20010-020";

        // Act
        AddressRequestDto addressRequestDto = viaCepApi.getCompleteAddress(cep);

        // Assert
        assertEquals("RJ", addressRequestDto.getUf());
        assertEquals("Rio de Janeiro", addressRequestDto.getLocalidade());
        assertEquals("Centro", addressRequestDto.getBairro());
        assertEquals("Rua São José", addressRequestDto.getLogradouro());

    }

    @Test
    @DisplayName("Must throw an InvalidInPutException when receives an invalid zip_code")
    void testGetCompleteAddressWithInvalidZipCode() {
        // Arrange
        String cep = "99999-999";

        // Act + Assert
        assertThrows(InvalidInputException.class, () -> viaCepApi.getCompleteAddress(cep));
    }
}