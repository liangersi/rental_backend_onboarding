package rental.presentation.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import rental.application.HouseApplicationService;
import rental.domain.model.House;
import rental.domain.model.enums.HouseStatus;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void should_get_one_house_info() throws Exception{
        House house = House.builder().name("house-test")
                .id(3333L)
                .price(BigDecimal.valueOf(3000))
                .status(HouseStatus.PENDING)
                .location("chengdu")
                .createdTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .establishedTime(LocalDateTime.of(2012, 8, 14, 12, 20, 0))
                .updatedTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .build();
        when(applicationService.queryOneHouseInfo(house.getId())).thenReturn(house);

        mvc.perform(get("/houses/"+house.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3333)));
    }

    @Test
    public void should_throw_not_found_exception_when_id_is_not_exist() throws Exception{
        when(applicationService.queryOneHouseInfo(any())).thenThrow(
                new NotFoundException("Not Found Exception")
        );
        mvc.perform(get("/houses/666")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Not Found Exception")));
    }
}
