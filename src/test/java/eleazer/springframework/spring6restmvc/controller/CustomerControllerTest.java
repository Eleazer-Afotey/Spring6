package eleazer.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eleazer.springframework.spring6restmvc.model.CustomerDTO;
import eleazer.springframework.spring6restmvc.service.CustomerService;
import eleazer.springframework.spring6restmvc.service.CustomerServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testPatchBeer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);
        Map<String, Object> mapCustomer = new HashMap<>();
        mapCustomer.put("customerName", "Donroc Huios");

        mockMvc.perform(patch("/api/v1/customer/patch/" + customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapCustomer)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomer(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(mapCustomer.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDTO customer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Eleazer Afotey")
                .version(new Random().nextInt())
                .lastModifiedDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();

        given(customerService.removeCustomer(any())).willReturn(true);

        mockMvc.perform(delete("/api/v1/customer/delete/"+customer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).removeCustomer(uuidArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        given(customerService.updateCustomer(any(), any())).willReturn(Optional.of(customer));

        mockMvc.perform(put("/api/v1/customer/update/"+customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"));

        verify(customerService).updateCustomer(uuidArgumentCaptor.capture(), any(CustomerDTO.class));
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }


    @Test
    void testInsertCustomer() throws Exception {

        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);
        customer.setId(null);

        given(customerService.insertCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.getAllCustomers().get(1));

        mockMvc.perform(post("/api/v1/customer/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void getAllCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        mockMvc.perform(get("/api/v1/customer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(customerServiceImpl.getAllCustomers().size())));
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);
        given(customerService.getCustomerById(customer.getId())).willReturn(Optional.of(customer));

        mockMvc.perform(get("/api/v1/customer/"+customer.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customer.getId().toString())));
    }


    @Test
    void getBeerByIdNotFound() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/customer/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}