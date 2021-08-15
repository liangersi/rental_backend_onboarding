package rental.presentation.controller;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import rental.application.HouseApplicationService;
import rental.domain.model.House;
import rental.domain.model.enums.HouseStatus;
import rental.presentation.dto.response.house.HouseResponse;
import rental.presentation.exception.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HouseControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private HouseApplicationService applicationService;

    @Test
    public void should_get_all_houses() throws Exception {
        // given
        List<House> houseList = Arrays.asList(
                House.builder().id(1L).name("name-1").build(),
                House.builder().id(2L).name("name-2").build());
        Page<House> housePage = new PageImpl<>(houseList);
        when(applicationService.queryAllHouses(any())).thenReturn(housePage);

        // when
        mvc.perform(get("/houses").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    public void should_get_one_house_info() throws Exception {
        House house = House.builder()
                .name("house-test")
                .id(3333L)
                .price(BigDecimal.valueOf(3000))
                .status(HouseStatus.PENDING)
                .location("chengdu")
                .createdTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .establishedTime(LocalDateTime.of(2012, 8, 14, 12, 20, 0))
                .updatedTime(LocalDateTime.of(2021, 8, 14, 12, 20, 0))
                .build();
        when(applicationService.queryOneHouseInfo(house.getId())).thenReturn(house);

        mvc.perform(get("/houses/" + house.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id", is(3333)));
    }

    @Test
    public void should_throw_not_found_exception_when_id_is_not_exist() throws Exception {
        when(applicationService.queryOneHouseInfo(any())).thenThrow(
                new NotFoundException("Not Found Exception")
        );
        mvc.perform(get("/houses/666")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Not Found Exception")));
    }

    @Test
    public void should_return_house_response_when_given_correct_house_request() throws Exception {
        HouseResponse houseResponse = HouseResponse.builder()
                .name("house-test")
                .price(BigDecimal.valueOf(3000))
                .status(HouseStatus.PENDING)
                .location("chengdu")
                .createdTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .establishedTime(LocalDateTime.of(2012, 8, 14, 12, 20, 0))
                .updatedTime(LocalDateTime.of(2021, 8, 14, 12, 20, 0))
                .build();

        when(applicationService.saveHouseInfo(any())).thenReturn(houseResponse);

        String houseJson = "{\"name\":\"house-test\",\"location\":\"chengdu\","
                + "\"price\":3000,\"establishedTime\":\"2012-08-14T12:20:00\",\"status\":\"PENDING\","
                + "\"createdTime\":\"2020-08-14T12:20:00\",\"updatedTime\":\"2021-08-14T12:20:00\"}";
        MockHttpServletRequestBuilder houseRequest = post("/houses/house")
                .contentType(MediaType.APPLICATION_JSON)
                .content(houseJson);

        MediaType contentType = new MediaType("application", "json");
        MockHttpServletResponse response = mvc.perform(houseRequest)
                .andExpect(content().contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("house-test")))
                .andExpect(jsonPath("$.price", is(3000)))
                .andExpect(jsonPath("$.location", is("chengdu")))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.createdTime", is("2020-08-14T12:20:00")))
                .andExpect(jsonPath("$.establishedTime", is("2012-08-14T12:20:00")))
                .andExpect(jsonPath("$.updatedTime", is("2021-08-14T12:20:00")))
                .andReturn()
                .getResponse();

        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    public void should_throw_exception_when_lack_param() throws Exception {
        String houseJsonLack = "{\"name\":\"house-test\",\"location\":\"chengdu\","
                + "\"status\":\"PENDING\",\"createdTime\":\"2020-08-14T12:20:00\","
                + "\"updatedTime\":\"2021-08-14T12:20:00\"}";
        MockHttpServletRequestBuilder requestBuilder = post("/houses/house")
                .contentType(MediaType.APPLICATION_JSON)
                .content(houseJsonLack);
        MediaType contentType = new MediaType("application", "json");

        mvc.perform(requestBuilder)
                .andExpect(content().contentType(contentType))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();
    }
}
