package rental.application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import rental.domain.model.House;
import rental.domain.model.enums.HouseStatus;
import rental.domain.repository.HouseRepository;
import rental.presentation.exception.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HouseApplicationServiceTest {
    @InjectMocks
    private HouseApplicationService applicationService;

    @Mock
    private HouseRepository repository;

    @Test
    public void should_get_all_houses() {
        // given
        List<House> houseList = Arrays.asList(
                House.builder().id(1L).name("name-1").build(),
                House.builder().id(2L).name("name-2").build());
        Page<House> housePage = new PageImpl<>(houseList);
        when(repository.queryAllHouses(any())).thenReturn(housePage);
        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<House> result = applicationService.queryAllHouses(pageable);

        // then
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void should_get_all_information(){
        House houseInfo = House.builder()
                .id(1L)
                .name("house-test")
                .price(BigDecimal.valueOf(3000))
                .status(HouseStatus.PENDING)
                .location("chengdu")
                .createdTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .establishedTime(LocalDateTime.of(2012, 8, 14, 12, 20, 0))
                .updatedTime(LocalDateTime.of(2021, 8, 14, 12, 20, 0))
                .build();
        when(repository.queryOneHouseInfo(any())).thenReturn(java.util.Optional.ofNullable(houseInfo));

        House result = applicationService.queryOneHouseInfo(1L);

        assertEquals(java.util.Optional.of(1L), java.util.Optional.of(result.getId()));
        assertEquals("house-test", result.getName());
        assertEquals(BigDecimal.valueOf(3000), result.getPrice());
        assertEquals(HouseStatus.PENDING, result.getStatus());
        assertEquals("chengdu", result.getLocation());
        assertEquals(LocalDateTime.of(2020, 8, 14, 12, 20, 0), result.getCreatedTime());
        assertEquals(LocalDateTime.of(2012, 8, 14, 12, 20, 0), result.getEstablishedTime());
        assertEquals(LocalDateTime.of(2021, 8, 14, 12, 20, 0), result.getUpdatedTime());
    }

    @Test
    public void should_throw_not_found_exception_when_id_is_not_exist(){
        when(repository.queryOneHouseInfo(any())).thenReturn(Optional.empty());
        String message = "";
        try {
            applicationService.queryOneHouseInfo(3333L);
        } catch (NotFoundException exception) {
            message = exception.getMessage();
        }
        assertEquals("not found exception", message);
    }
}
