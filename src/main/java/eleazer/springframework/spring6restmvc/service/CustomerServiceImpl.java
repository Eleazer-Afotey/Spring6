package eleazer.springframework.spring6restmvc.service;

import eleazer.springframework.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final HashMap<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl(){
        this.customerMap = new HashMap<>();

        CustomerDTO customerOne = CustomerDTO
                .builder()
                .id(UUID.randomUUID())
                .customerName("Arianna Denee")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customerTwo = CustomerDTO
                .builder()
                .id(UUID.randomUUID())
                .customerName("Gladys Tawiah")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customerThree = CustomerDTO
                .builder()
                .id(UUID.randomUUID())
                .customerName("Eleazer Afotey")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(customerOne.getId(), customerOne);
        customerMap.put(customerTwo.getId(), customerTwo);
        customerMap.put(customerThree.getId(), customerThree);

    }


    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Boolean removeCustomer(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public CustomerDTO insertCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer =  CustomerDTO.builder().
                id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .version(new Random().nextInt())
                .lastModifiedDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);
        log.info(MessageFormat.format("Newly created Customer with id {0} saved to the DB", savedCustomer.getId()));

        return savedCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {


        CustomerDTO updatedCustomer = CustomerDTO.builder()
                        .id(customerId)
                        .createdDate(customer.getCreatedDate())
                        .version(customer.getVersion())
                        .customerName(customer.getCustomerName())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

        customerMap.put(customerId, updatedCustomer);
        log.info(MessageFormat.format("Customer with ID {0} was updated", updatedCustomer.getId()));

        return Optional.of(updatedCustomer);
    }

    @Override
    public void patchCustomer(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())){
            existing.setCustomerName(customer.getCustomerName());
        }

        if(customer.getVersion() != null){
            existing.setVersion(customer.getVersion());
        }

        if(customer.getCreatedDate() != null){
            existing.setCreatedDate(customer.getCreatedDate());
        }

        if(customer.getLastModifiedDate() != null){
            existing.setLastModifiedDate(customer.getLastModifiedDate());
        }
    }


}
