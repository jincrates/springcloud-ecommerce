package me.jincrates.orderservice.controller;

import lombok.RequiredArgsConstructor;
import me.jincrates.orderservice.controller.request.RequestOrder;
import me.jincrates.orderservice.controller.response.ResponseOrder;
import me.jincrates.orderservice.dto.OrderDto;
import me.jincrates.orderservice.jpa.OrderEntity;
import me.jincrates.orderservice.messagequeue.KafkaProducer;
import me.jincrates.orderservice.messagequeue.OrderProducer;
import me.jincrates.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final Environment env;
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    // http://127.0.0.1/order-service/{userId}/orders/
    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody RequestOrder request) {
        OrderDto orderDto = modelMapper.map(request, OrderDto.class);
        orderDto.setUserId(userId);

        /* jpa */
//        OrderDto createdOrder = orderService.createOrder(orderDto);
//        ResponseOrder responseOrder = modelMapper.map(createdOrder, ResponseOrder.class);

        /* kafka */
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(request.getQty() * request.getUnitPrice());

        /* send this order to the kafka */
        kafkaProducer.send("example-catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);

        ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    // http://127.0.0.1/order-service/{userId}/orders/
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) {
        Iterable<OrderEntity> orders = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orders.forEach(o -> {
            result.add(modelMapper.map(o, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
