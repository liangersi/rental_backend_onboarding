package rental.infrastructure.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import rental.domain.model.House;
import rental.domain.model.enums.HouseStatus;
import rental.infrastructure.dataentity.HouseEntity;
import rental.infrastructure.persistence.HouseJpaPersistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class HouseRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private HouseJpaPersistence persistence;

    private HouseRepositoryImpl repository;

    @Before
    public void setUp() {
        repository = new HouseRepositoryImpl(persistence);
    }

    @Test
    public void should_find_2_houses_with_page() {
        // given
        entityManager.persistAndFlush(HouseEntity.builder().name("house-1").build());
        entityManager.persistAndFlush(HouseEntity.builder().name("house-2").build());
        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<House> result = this.repository.queryAllHouses(pageable);

        // then
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void should_find_21_houses_with_page() {
        // given
        for (int index = 1; index <= 21; index++) {
            entityManager.persistAndFlush(HouseEntity.builder().name("house-" + index).build());
        }
        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<House> result = this.repository.queryAllHouses(pageable);

        // then
        assertEquals(21, result.getTotalElements());
        assertEquals(20, result.getContent().size());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    public void should_find_0_houses_with_page() {
        // given
        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<House> result = this.repository.queryAllHouses(pageable);

        // then
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
    }

    @Test
    public void should_find_1_house_info_with_house_id() {
        HouseEntity houseEntity = entityManager.persistAndFlush(HouseEntity.builder()
                .name("house-test")
                .price(BigDecimal.valueOf(3000))
                .status(HouseStatus.PENDING)
                .location("chengdu")
                .createdTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .establishedTime(LocalDateTime.of(2012, 8, 14, 12, 20, 0))
                .updatedTime(LocalDateTime.of(2021, 8, 14, 12, 20, 0))
                .build());

        // when
        Optional<House> house = this.repository.queryOneHouseInfo(houseEntity.getId());
        House result = new House();
        if (house.isPresent()) {
            result = house.get();
        }

        // then
        assertEquals("house-test", result.getName());
        assertEquals(BigDecimal.valueOf(3000), result.getPrice());
        assertEquals(HouseStatus.PENDING, result.getStatus());
        assertEquals("chengdu", result.getLocation());
        assertEquals(LocalDateTime.of(2020, 8, 14, 12, 20, 0), result.getCreatedTime());
        assertEquals(LocalDateTime.of(2012, 8, 14, 12, 20, 0), result.getEstablishedTime());
        assertEquals(LocalDateTime.of(2021, 8, 14, 12, 20, 0), result.getUpdatedTime());
    }

    @Test
    public void should_return_house_when_given_correct_house_info() {
        HouseEntity houseEntity = entityManager.persistAndFlush(HouseEntity.builder()
                .name("house-test")
                .price(BigDecimal.valueOf(3000))
                .status(HouseStatus.PENDING)
                .location("chengdu")
                .createdTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .establishedTime(LocalDateTime.of(2012, 8, 14, 12, 20, 0))
                .updatedTime(LocalDateTime.of(2021, 8, 14, 12, 20, 0))
                .build());
        House result = this.repository.saveHouseInfo(houseEntity);

        assertEquals("house-test", result.getName());
        assertEquals(BigDecimal.valueOf(3000), result.getPrice());
        assertEquals(HouseStatus.PENDING, result.getStatus());
        assertEquals("chengdu", result.getLocation());
        assertEquals(LocalDateTime.of(2020, 8, 14, 12, 20, 0), result.getCreatedTime());
        assertEquals(LocalDateTime.of(2012, 8, 14, 12, 20, 0), result.getEstablishedTime());
        assertEquals(LocalDateTime.of(2021, 8, 14, 12, 20, 0), result.getUpdatedTime());
    }

    @Test
    public void should_delete_house_info() {
        HouseEntity houseEntity = entityManager.persistAndFlush(HouseEntity.builder()
                .name("house-test")
                .price(BigDecimal.valueOf(3000))
                .status(HouseStatus.PENDING)
                .location("chengdu")
                .createdTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .establishedTime(LocalDateTime.of(2012, 8, 14, 12, 20, 0))
                .updatedTime(LocalDateTime.of(2021, 8, 14, 12, 20, 0))
                .build());
        this.repository.deleteHouse(houseEntity);
        Optional<House> houseOptional = this.repository.queryOneHouseInfo(houseEntity.getId());

        assertNotEquals("house-test", houseOptional.<Object>map(House::getName).orElse(null));

    }
}
