package eleazer.springframework.spring6restmvc.service;

import eleazer.springframework.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO insertBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer);

    Boolean deleteBeer(UUID id);

    Optional<BeerDTO> patchBeer(UUID id, BeerDTO beer);
}
