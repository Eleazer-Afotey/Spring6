package eleazer.springframework.spring6restmvc.boostrap;


import eleazer.springframework.spring6restmvc.entities.Beer;
import eleazer.springframework.spring6restmvc.entities.Customer;
import eleazer.springframework.spring6restmvc.model.BeerStyle;
import eleazer.springframework.spring6restmvc.repository.BeerRepository;
import eleazer.springframework.spring6restmvc.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    private final CustomerRepository customerRepository;
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData(){

        if(beerRepository.count() == 0){
            Beer beer1 = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("123456")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(122)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Crank")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("123456")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(232)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Coitus")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("123453")
                    .price(new BigDecimal("17.99"))
                    .quantityOnHand(152)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.saveAll(Arrays.asList(beer1,beer2,beer3));
        }



    }

    private void loadCustomerData(){

        if(customerRepository.count() == 0){
            Customer customerOne = Customer
                    .builder()
                    .id(UUID.randomUUID())
                    .customerName("Arianna Denee")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customerTwo = Customer
                    .builder()
                    .id(UUID.randomUUID())
                    .customerName("Gladys Tawiah")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customerThree = Customer
                    .builder()
                    .id(UUID.randomUUID())
                    .customerName("Eleazer Afotey")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(customerOne, customerTwo, customerThree));
        }
    }


}
