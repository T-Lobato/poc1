package com.insiders.poc1.controller.unitary;

import static org.junit.jupiter.api.Assertions.*;

import com.insiders.poc1.controller.CustomerController;
import com.insiders.poc1.entities.Customer;
import com.insiders.poc1.enums.PersonType;
import com.insiders.poc1.service.CustomerService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    private Customer customer;

    static String CUSTOMER_API = "/api/poc1/customers";

    protected void saveCustomer(){
        customer = Customer.builder()
                .name("Thyago")
                .personType(PersonType.PF)
                .email("thyagollobato@gmail.com")
                .phoneNumber("15981229370")
                .addressList(new ArrayList<>())
                .build();
    }


    @Test
    @DisplayName("Must sucessfully create a customer")
    void saveACustomer() throws Exception {
        URI uri = new URI(CUSTOMER_API);
        String json = "{\"name\":\"Thyago\"," +
                "\"document\":\"12633821774\"," +
                "\"personType\":\"PF\"," +
                "\"email\":\"thyagollobato@gmail.com\"," +
                "\"phoneNumber\":\"15981229370\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.
                        status()
                        .is(201));
    }
}