package eleazer.springframework.spring6restmvc.controller;

import eleazer.springframework.spring6restmvc.model.BeerDTO;
import eleazer.springframework.spring6restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/beer")
public class BeerController {

    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET )
    public List<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @RequestMapping("{beerId}")
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get beer by id - value is " + beerId.toString());
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);

    }

    @PostMapping(path = "/insert")
    public ResponseEntity<BeerDTO> insertBeer(@Validated @RequestBody BeerDTO beer) {

        BeerDTO savedbeer = beerService.insertBeer(beer);
        log.info(MessageFormat.format("Inserted Beer with id {0}", savedbeer.getId()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/v1/beer/insert/"+savedbeer.getId().toString());


        return new ResponseEntity<>( httpHeaders, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/update/{beerId}", method = RequestMethod.PUT)
    public ResponseEntity<BeerDTO> updateBeer(@PathVariable("beerId") UUID id, @Validated @RequestBody BeerDTO beer){
        if(beerService.updateBeer(id, beer).isEmpty()){
            throw new NotFoundException();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "api/v1/update/"+ id);

        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/delete/{beerId}")
    public ResponseEntity<BeerDTO> deleteBeer(@PathVariable("beerId") UUID id){
        if(! beerService.deleteBeer(id)){
           throw new NotFoundException();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/V1/beer/delete/"+id);

        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/patch/{beerId}")
    public ResponseEntity<BeerDTO> patchBeer(@PathVariable("beerId") UUID id, @RequestBody BeerDTO beer){
        beerService.patchBeer(id,beer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "api/v1/beer/patch/"+id);

        return  new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }

    //Exception Handling


}
