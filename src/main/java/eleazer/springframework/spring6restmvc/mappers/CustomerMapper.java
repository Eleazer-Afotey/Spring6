package eleazer.springframework.spring6restmvc.mappers;

import eleazer.springframework.spring6restmvc.entities.Customer;
import eleazer.springframework.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDto(Customer customer);
}
