package rental.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rental.domain.model.House;
import rental.infrastructure.dataentity.HouseEntity;

import java.util.Optional;

public interface HouseRepository {
    Page<House> queryAllHouses(Pageable pageable);

    Optional<House> queryOneHouseInfo(Long houseId);

    House saveHouseInfo(HouseEntity houseEntity);
}
