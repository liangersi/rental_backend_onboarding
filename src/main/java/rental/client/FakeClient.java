package rental.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rental.domain.model.House;

@FeignClient("fakeApi-service")
public interface FakeClient {

    @PostMapping("houses/house")
    House saveOneHouseInfo(@RequestBody House house);
}
