package rental.presentation.assembler;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rental.domain.model.House;
import rental.presentation.dto.request.HouseRequest;
import rental.utils.DateUtils;

import java.time.LocalDateTime;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequestToModelMapper {

    RequestToModelMapper INSTANCE = Mappers.getMapper(RequestToModelMapper.class);

    @Mapping(target = "establishedTime", source = "houseRequest.establishedTime", qualifiedByName = "toTimestamp")
    @Mapping(target = "createdTime", source = "houseRequest.createdTime", qualifiedByName = "toTimestamp")
    @Mapping(target = "updatedTime", source = "houseRequest.updatedTime", qualifiedByName = "toTimestamp")
    House mapToPromotionProposalModel(HouseRequest houseRequest);

    @Named("toTimestamp")
    default long toTimestamp(LocalDateTime localDateTime) {
        return DateUtils.toTimestamp(localDateTime);
    }
}
