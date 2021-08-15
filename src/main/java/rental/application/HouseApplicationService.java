package rental.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rental.client.FakeClient;
import rental.domain.model.House;
import rental.domain.repository.HouseRepository;
import rental.infrastructure.dataentity.HouseEntity;
import rental.presentation.assembler.ModelToResponseMapper;
import rental.presentation.assembler.RequestToEntityMapper;
import rental.presentation.dto.request.HouseRequest;
import rental.presentation.dto.response.house.HouseResponse;
import rental.presentation.exception.AddThirdClientException;
import rental.presentation.exception.NotFoundException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HouseApplicationService {
    private final HouseRepository houseRepository;
    private final FakeClient fakeClient;

    public Page<House> queryAllHouses(Pageable pageable) {
        return houseRepository.queryAllHouses(pageable);
    }

    public House queryOneHouseInfo(Long houseId) {
        Optional<House> houseOptional = houseRepository.queryOneHouseInfo(houseId);
        houseOptional.orElseThrow(() -> new NotFoundException("not found exception"));
        return houseOptional.get();
    }

    public HouseResponse saveHouseInfo(HouseRequest houseRequest) throws AddThirdClientException {
        HouseEntity houseEntity = RequestToEntityMapper.INSTANCE.mapToPromotionProposalModel(houseRequest);
        House houseSaved = houseRepository.saveHouseInfo(houseEntity);

        if (!fakeClient.saveOneHouseInfo(houseSaved)) {
            houseRepository.deleteHouse(houseEntity);
            throw new AddThirdClientException("fail update info to 3rd client");
        }
        return ModelToResponseMapper.INSTANCE.mapToPromotionProposalResponse(houseSaved);
    }
}
