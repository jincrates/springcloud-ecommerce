package me.jincrates.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.orderservice.dto.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static me.jincrates.orderservice.dto.KafkaOrderDto.createKafkaOrderDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderDto send(String topic, OrderDto orderDto) {

        KafkaOrderDto kafkaOrderDto = createKafkaOrderDto(orderDto);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();;
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Order Producer send data from the Order microservice: {}", kafkaOrderDto);

        return orderDto;
    }
}
