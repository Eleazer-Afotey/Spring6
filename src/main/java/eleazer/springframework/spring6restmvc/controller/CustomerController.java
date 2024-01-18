package eleazer.springframework.spring6restmvc.controller;

import eleazer.springframework.spring6restmvc.model.CustomerDTO;
import eleazer.springframework.spring6restmvc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomerDTO> getAllCustomers(){
        log.info("Returned all customers from controller");
        return customerService.getAllCustomers();
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId){
        log.info(MessageFormat.format("Returnee Customer with id {0}", customerId));
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);

    }

    @RequestMapping(value = "/delete/{customerId}", method = RequestMethod.DELETE)
    public ResponseEntity<CustomerDTO> removeCustomer(@PathVariable("customerId") UUID customerId){

        if(!customerService.removeCustomer(customerId)){
            throw new NotFoundException();
        }

        ;
        log.debug(MessageFormat.format("Deleted customer with the id {0}", customerId));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/v1/customer/delete/" +customerId);
        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseEntity<CustomerDTO> insertCustomer(@RequestBody CustomerDTO customer){
       CustomerDTO savedCustomer = customerService.insertCustomer(customer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/v1/customer/insert/" + savedCustomer.getId());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(path = "/update/{customer}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("customer") UUID id, @RequestBody CustomerDTO customer){
        if(customerService.updateCustomer(id,customer).isEmpty()){
            throw new NotFoundException();
       }


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "api/v1/customer/update/"+id);

        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }


    @PatchMapping(path = "/patch/{customer}")
    public ResponseEntity<CustomerDTO> patchCustomer(@PathVariable("customer") UUID id, @RequestBody CustomerDTO customer){
        customerService.patchCustomer(id,customer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "api/v1/customer/update/"+id);

        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }





}
