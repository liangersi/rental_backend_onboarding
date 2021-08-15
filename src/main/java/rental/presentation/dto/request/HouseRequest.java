package rental.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rental.domain.model.enums.HouseStatus;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HouseRequest {
    private Long id;

    private String name;

    @NotNull
    private String location;

    @NotNull
    private BigDecimal price;

    @NotNull
    private LocalDateTime establishedTime;

    private HouseStatus status;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
