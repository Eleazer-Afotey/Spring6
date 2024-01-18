package eleazer.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eleazer.springframework.spring6restmvc.entities.Beer;
import eleazer.springframework.spring6restmvc.mappers.BeerMapper;
import eleazer.springframework.spring6restmvc.model.BeerDTO;
import eleazer.springframework.spring6restmvc.repository.BeerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
class BeerControllerIntegrationTest {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testPatchBeerBadName() throws Exception{
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> map = new HashMap<>();
        map.put("beerName", "Scotch23243423232193732457371742345723457235734572353745234734752353757537523345-0489-0589-058-08456345");

       mockMvc.perform(patch("/api/v1/beer/patch/" + beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void listBeers() {
        List<BeerDTO> beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    void testEmptyBeer() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);

    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerController.getBeerById(beer.getId());
        assertThat(beer.getId()).isNotNull();
        assertThat(beerDTO).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void testInsertBeer() {
        BeerDTO  beerDTO = BeerDTO.builder()
                .beerName("Another One")
                .build();

        ResponseEntity<BeerDTO> responseEntity = beerController.insertBeer(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders()).isNotNull();
        log.info(responseEntity.getHeaders().getLocation().toString());

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID uuid = UUID.fromString(locationUUID[5]);
        log.info(MessageFormat.format("The id is {0}", uuid));


        assertThat(beerRepository.findById(uuid)).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void testUpdateBeer() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        beerDTO.setBeerName("UPDATED");
        ResponseEntity<BeerDTO> responseEntity = beerController.updateBeer(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo("UPDATED");
    }
    @Test
    void testDeleteNotFoundException(){
        assertThrows(NotFoundException.class, () ->{
            beerController.deleteBeer(UUID.randomUUID());
        });
    }

    @Test
    void testUpdateNotFoundException(){
        assertThrows(NotFoundException.class, () ->{
            beerController.updateBeer(UUID.randomUUID(), beerMapper.beerToBeerDto(beerRepository.findAll().get(1)));
        });
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteBeer() {

        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity responseEntity = beerController.deleteBeer(beer.getId());

        assertThat(beerRepository.findById(beer.getId())).isEmpty();

    }

    @Test
    void patchBeer() {
    }
}