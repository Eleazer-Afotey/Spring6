package eleazer.springframework.spring6restmvc.service;

import eleazer.springframework.spring6restmvc.model.BeerDTO;
import eleazer.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl(){
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(232)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Coitus")
                .beerStyle(BeerStyle.LAGER)
                .upc("123453")
                .price(new BigDecimal("17.99"))
                .quantityOnHand(152)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<BeerDTO> listBeers(){
        return new ArrayList<>(beerMap.values());
    }


    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.debug("In the Beer Controller");

        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO insertBeer(BeerDTO beerPut) {
        BeerDTO beer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName(beerPut.getBeerName())
                .version(beerPut.getVersion())
                .upc(beerPut.getUpc())
                .quantityOnHand(beerPut.getQuantityOnHand())
                .price(beerPut.getPrice().add(BigDecimal.valueOf(340)))
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        beerMap.put(beer.getId(), beer);


        return beer;
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer) {
        BeerDTO updateBeer = beerMap.get(id);

                updateBeer.setQuantityOnHand(beer.getQuantityOnHand());
                updateBeer.setBeerName(beer.getBeerName());
                updateBeer.setBeerStyle(beer.getBeerStyle());
                updateBeer.setUpdateDate(LocalDateTime.now());
                updateBeer.setUpc(beer.getUpc());

        beerMap.put(updateBeer.getId(), updateBeer);

        return Optional.of(updateBeer);

    }

    @Override
    public Boolean deleteBeer(UUID id) {
        beerMap.remove(id);

        return true;
    }

    @Override
    public Optional<BeerDTO> patchBeer(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);

        if (StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }

        if (beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }

        return Optional.of(existing);
    }


}
