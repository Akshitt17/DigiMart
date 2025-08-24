package com.smarthome.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.customer.dto.AddressRequest;
import com.smarthome.customer.dto.CustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userIdHeader;

    @BeforeEach
    void setup() {
        userIdHeader = "99";
    }

    // 1. Register New Customer
    @Test
    public void registerCustomer_shouldReturnCreated() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setFullName("Sample User");
        request.setEmail("sampleuser99@example.com");
        request.setPhone("+919999999999");

        mockMvc.perform(post("/api/v1/customers/register")
                .header("X-User-Id", userIdHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName", is("Sample User")));
    }

    // 2. Get Customer by Customer ID
    @Test
    public void getCustomerById_shouldReturnCustomer() throws Exception {
        // Register first
        CustomerRequest request = new CustomerRequest();
        request.setFullName("Tester");
        request.setEmail("tester@example.com");
        request.setPhone("+911111111111");

        String json = mockMvc.perform(post("/api/v1/customers/register")
                .header("X-User-Id", "555")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long customerId = objectMapper.readTree(json).get("customerId").asLong();

        mockMvc.perform(get("/api/v1/customers/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId));
    }

    // 3. Get Customer By User Id
    @Test
    public void getCustomerByUserId_shouldReturnCustomer() throws Exception {
        mockMvc.perform(get("/api/v1/customers/by-user/" + userIdHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", notNullValue()));
    }

    // 4. Update Customer Profile
    @Test
    public void updateCustomer_shouldReturnOk() throws Exception {
        // Register user to get customerId
        CustomerRequest toRegister = new CustomerRequest();
        toRegister.setFullName("User Updatable");
        toRegister.setEmail("updatable@example.com");
        toRegister.setPhone("+918888888888");

        String json = mockMvc.perform(post("/api/v1/customers/register")
                .header("X-User-Id", "888")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toRegister)))
                .andReturn().getResponse().getContentAsString();

        Long customerId = objectMapper.readTree(json).get("customerId").asLong();

        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setFullName("Updated Name");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhone("+917777777777");

        mockMvc.perform(put("/api/v1/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is("Updated Name")));
    }

    // 5. Add Address for Customer
    @Test
    public void addAddress_shouldReturnCreated() throws Exception {
        // Register user to get customerId
        CustomerRequest toRegister = new CustomerRequest();
        toRegister.setFullName("Address Adder");
        toRegister.setEmail("address@adder.com");
        toRegister.setPhone("+911234567700");

        String json = mockMvc.perform(post("/api/v1/customers/register")
                .header("X-User-Id", "777")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toRegister)))
                .andReturn().getResponse().getContentAsString();

        Long customerId = objectMapper.readTree(json).get("customerId").asLong();

        AddressRequest addr = new AddressRequest();
        addr.setStreetAddress("221B Baker Street");
        addr.setCity("London");
        addr.setState("LN");
        addr.setPostalCode("NW16XE");
        addr.setAddressType("HOME");
        addr.setIsDefault(true);

        mockMvc.perform(post("/api/v1/customers/" + customerId + "/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addr)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("success")));
    }

    // 6. Get All Addresses for Customer
    @Test
    public void getAddresses_shouldReturnList() throws Exception {
        // Register user and add address to ensure list is not empty
        CustomerRequest toRegister = new CustomerRequest();
        toRegister.setFullName("Addr Getter");
        toRegister.setEmail("addr@getter.com");
        toRegister.setPhone("+919191919191");

        String json = mockMvc.perform(post("/api/v1/customers/register")
                .header("X-User-Id", "666")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toRegister)))
                .andReturn().getResponse().getContentAsString();

        Long customerId = objectMapper.readTree(json).get("customerId").asLong();

        AddressRequest addr = new AddressRequest();
        addr.setStreetAddress("1 Infinite Loop");
        addr.setCity("Cupertino");
        addr.setState("CA");
        addr.setPostalCode("95014");
        addr.setAddressType("OFFICE");
        addr.setIsDefault(false);

        mockMvc.perform(post("/api/v1/customers/" + customerId + "/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addr)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/customers/" + customerId + "/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].city", notNullValue()));
    }
}
