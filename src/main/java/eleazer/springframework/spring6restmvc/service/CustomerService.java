package eleazer.springframework.spring6restmvc.service;

import eleazer.springframework.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID id);

    List<CustomerDTO> getAllCustomers();

    Boolean removeCustomer(UUID customerId);

    CustomerDTO insertCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer);
    void patchCustomer(UUID customerId, CustomerDTO customer);
}

