package rental.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rental.domain.model.House;
import rental.domain.repository.HouseRepository;
import rental.infrastructure.dataentity.HouseEntity;
import rental.infrastructure.mapper.EntityToModelMapper;
import rental.infrastructure.persistence.HouseJpaPersistence;

import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class HouseRepositoryImpl implements HouseRepository {
    private final HouseJpaPersistence persistence;

    @Override
    public Page<House> queryAllHouses(Pageable pageable) {
        return this.persistence.findAll(pageable).map(EntityToModelMapper.INSTANCE::mapToModel);
    }

    @Override
    public Optional<House> queryOneHouseInfo(Long houseId) {
        return this.persistence.findById(houseId).map(EntityToModelMapper.INSTANCE::mapToModel);
    }

    @Override
    public House saveHouseInfo(HouseEntity houseEntity) {
        return EntityToModelMapper.INSTANCE.mapToModel(this.persistence.save(houseEntity));
    }
}
