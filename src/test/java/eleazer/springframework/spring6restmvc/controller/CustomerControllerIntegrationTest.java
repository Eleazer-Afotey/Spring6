package eleazer.springframework.spring6restmvc.controller;

import eleazer.springframework.spring6restmvc.entities.Customer;
import eleazer.springframework.spring6restmvc.model.CustomerDTO;
import eleazer.springframework.spring6restmvc.repository.BeerRepository;
import eleazer.springframework.spring6restmvc.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CustomerControllerIntegrationTest {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;


    @Test
    void getAllCustomers() {
       List<CustomerDTO> customerDTOList = customerController.getAllCustomers();
        assertThat(customerDTOList.size()).isEqualTo(3);
    }

    @Test
    void getCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
        assertThat(customerDTO).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void testEmptyCustomerRepo() {
        customerRepository.deleteAll();
        assertThat(customerRepository.count()).isEqualTo(0);
    }

    @Test
    void testGetByIdNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testUpdateNotFoundException(){
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomer(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }


    @Test
    @Transactional
    @Rollback
    void testRemoveCustomer() {
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity<CustomerDTO> responseEntity = customerController.removeCustomer(customer.getId());
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testInsertCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("Test Customer")
                .build();

        ResponseEntity<CustomerDTO> responseEntity = customerController.insertCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID uuid = UUID.fromString(locationUUID[5]);
        assertThat(customerRepository.findById(uuid)).isNotNull();


    }

    @Test
    @Transactional
    @Rollback
    void updateCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("UPDATED")
                .createdDate(LocalDateTime.now())
                .build();

        ResponseEntity<CustomerDTO> responseEntity = customerController.updateCustomer(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo("UPDATED");
        
    }

    @Test
    void patchCustomer() {
    }
}