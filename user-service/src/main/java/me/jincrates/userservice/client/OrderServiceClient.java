package me.jincrates.userservice.client;

import me.jincrates.userservice.controller.response.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/order-service/{userId}/orders-ng")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
