package eleazer.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eleazer.springframework.spring6restmvc.entities.Beer;
import eleazer.springframework.spring6restmvc.model.BeerDTO;
import eleazer.springframework.spring6restmvc.service.BeerService;
import eleazer.springframework.spring6restmvc.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        Map<String, Object> map = new HashMap<>();
        map.put("beerName", "Scotch");

        mockMvc.perform(patch("/api/v1/beer/patch/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isNoContent());


        verify(beerService).patchBeer(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(map.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
        System.out.println(objectMapper.writeValueAsString(map));
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        given(beerService.deleteBeer(any())).willReturn(true);

        mockMvc.perform(delete("/api/v1/beer/delete/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeer(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        given(beerService.updateBeer(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put("/api/v1/beer/update/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)));

        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testCreateNewBeer() throws Exception {

        BeerDTO beer = beerServiceImpl.listBeers().get(0);
        beer.setId(null);

        given(beerService.insertBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post("/api/v1/beer/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));


    }

    @Test
    void testCreatedBeerNullBeerName() throws Exception {

        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.insertBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers().get(1));

       MvcResult mvcResult =  mockMvc.perform(post("/api/v1/beer/insert")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.length()", is(6)))
        .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());


    }

    @Test
    void testUpdateBeerNullBeerName() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers().get(0);
        beer.setBeerName("");

        given(beerService.updateBeer(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put("/api/v1/beer/update/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                        .andExpect(status().isBadRequest());

    }

    @Test
    void testListBeers() throws Exception {

        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(beerServiceImpl.listBeers().size())));

    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(beer.getId())).willReturn(Optional.of(beer));

        mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(beer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(beer.getBeerName())));
    }


    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}