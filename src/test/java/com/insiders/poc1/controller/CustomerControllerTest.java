package com.insiders.poc1.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insiders.poc1.controller.dto.request.CustomerRequestDto;
import com.insiders.poc1.controller.dto.response.CustomerResponseDto;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.service.CustomerService;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    ModelMapper mapper;

    static String CUSTOMER_API = "/api/poc1/customers";

    @Test
    @DisplayName("Must sucessfully create a customer")
    void saveACustomer() throws Exception {

        //Set up test data
        CustomerRequestDto customerRequestDto = CustomerRequestDto.builder()
                .name("Thyago")
                .document("12633821774")
                .personType(PersonType.PF)
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(1L)
                .name("guilherme")
                .personType(PersonType.PF)
                .document("12633821774")
                .email("gui@email.com")
                .phoneNumber("15981229370")
                .addressList(new ArrayList<>())
                .build();

        CustomerResponseDto expectedResponse = CustomerResponseDto.builder()
                .id(1L)
                .name("Thyago")
                .email("thyagollobato@gmail.com")
                .addressList(new ArrayList<>())
                .build();

        //Set up mock behavior
        BDDMockito.given(customerService.save(Mockito.any(CustomerRequestDto.class))).willReturn(savedCustomer);
        BDDMockito.given(mapper.map(savedCustomer, CustomerResponseDto.class)).willReturn(expectedResponse);

        // convert test data to JSON
        String json = new ObjectMapper().writeValueAsString(customerRequestDto);

        // Send POST request to the controller
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMER_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        // Convert response to an object
        CustomerResponseDto actualResponse =
                new ObjectMapper().readValue(result.getResponse().getContentAsString(), CustomerResponseDto.class);

        // Verify that the response is correct
        assertThat(actualResponse.getName()).isEqualTo(expectedResponse.getName());
        assertThat(actualResponse.getEmail()).isEqualTo(expectedResponse.getEmail());
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findCustomerByName() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void update() {
    }
}