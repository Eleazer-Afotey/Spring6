package eleazer.springframework.spring6restmvc.repository;

import eleazer.springframework.spring6restmvc.entities.Beer;
import eleazer.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer(){

        Beer savedBeer = beerRepository.save(Beer.builder()
                        .beerName("My beer")
                        .beerStyle(BeerStyle.PALE_ALE)
                        .price(BigDecimal.valueOf(18.99))
                        .upc("2342")
                        .build());

        beerRepository.flush();

        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer).isNotNull();

    }

    @Test
    void testSaveBeerNameTooLong(){

        assertThrows(ConstraintViolationException.class, () -> {
                Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My beer is 123-034i3t- 34034039403434234345t34t3ergregetgergtgdfghdsfgdfyhjdghdhjgfshrthjfgdnhndfgdh")
                .beerStyle(BeerStyle.PALE_ALE)
                .price(BigDecimal.valueOf(18.99))
                .upc("2342")
                .build());
        beerRepository.flush();
        });
    }

}