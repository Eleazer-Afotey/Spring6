package eleazer.springframework.spring6restmvc.service;

import eleazer.springframework.spring6restmvc.controller.NotFoundException;
import eleazer.springframework.spring6restmvc.entities.Customer;
import eleazer.springframework.spring6restmvc.mappers.CustomerMapper;
import eleazer.springframework.spring6restmvc.model.CustomerDTO;
import eleazer.springframework.spring6restmvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean removeCustomer(UUID customerId) {

        if(customerRepository.existsById(customerId)){
            customerRepository.deleteById(customerId);

            return true;
        }

        return false;
    }

    @Override
    public CustomerDTO insertCustomer(CustomerDTO customerDTO) {
        Customer saveCustomer = customerMapper.customerDtoToCustomer(customerDTO);

        return customerMapper.customerToCustomerDto(customerRepository.save(saveCustomer));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
       customerRepository.findById(customerId)
               .ifPresentOrElse((foundCustomer) -> {
                   foundCustomer.setCustomerName(customer.getCustomerName());
                   foundCustomer.setCreatedDate(customer.getCreatedDate());
                   foundCustomer.setLastModifiedDate(customer.getLastModifiedDate());
                   atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(foundCustomer))));
               },
                       () ->{
                   atomicReference.set(Optional.empty());
                       });

       return atomicReference.get();

    }

    @Override
    public void patchCustomer(UUID customerId, CustomerDTO customer) {

    }
}
