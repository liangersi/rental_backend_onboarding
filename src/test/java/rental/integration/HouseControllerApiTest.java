package rental.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rental.RentalServiceApplication;
import rental.config.BaseIntegrationTest;
import rental.domain.model.enums.HouseStatus;
import rental.infrastructure.dataentity.HouseEntity;
import rental.infrastructure.persistence.HouseJpaPersistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RentalServiceApplication.class
)
public class HouseControllerApiTest extends BaseIntegrationTest {
    @Autowired
    private ApplicationContext applicationContext;

    private HouseJpaPersistence persistence;

    @Before
    public void setUp() {
        persistence = applicationContext.getBean(HouseJpaPersistence.class);
    }

    @Test
    public void should_get_all_houses() throws Exception {
        // given
        persistence.saveAndFlush(HouseEntity.builder().name("house-1").build());
        persistence.saveAndFlush(HouseEntity.builder().name("house-2").build());

        // when
        given()
                .when()
                .get("/houses")
                .then()
                .statusCode(200)
                .body("totalElements", is(2))
                .body("content", hasSize(2));
    }

    @Test
    public void should_get_one_house_information() throws Exception {
        // given
        persistence.saveAndFlush(HouseEntity.builder()
                .id(1L)
                .name("house-test")
                .price(BigDecimal.valueOf(3000))
                .status(HouseStatus.PENDING)
                .location("chengdu")
                .createdTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .establishedTime(LocalDateTime.of(2012, 8, 14, 12, 20, 0))
                .updatedTime(LocalDateTime.of(2020, 8, 14, 12, 20, 0))
                .build());

        // when
        given()
                .when()
                .get("/houses/1")
                .then()
                .statusCode(302)
                .body("id", is(1))
                .body("name", is("house-test"))
                .body("price", is(3000))
                .body("status", is("PENDING"))
                .body("location", is("chengdu"))
                .body("createdTime", is("2020-08-14T12:20:00"))
                .body("establishedTime", is("2012-08-14T12:20:00"))
                .body("updatedTime", is("2020-08-14T12:20:00"));
    }
}
