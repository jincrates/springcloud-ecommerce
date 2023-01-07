package me.jincrates.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class KafkaOrderDto implements Serializable {
    private Schema schema;
    private Payload payload;

    public static KafkaOrderDto createKafkaOrderDto(OrderDto orderDto) {
        List<Field> fields = Arrays.asList(
                new Field("string", true, "order_id"),
                new Field("string", true, "user_id"),
                new Field("string", true, "product_id"),
                new Field("int32", true, "qty"),
                new Field("int32", true, "unit_price"),
                new Field("int32", true, "total_price")
        );

        Schema schema = Schema.builder()
                .type("struct")
                .fields(fields)
                .optional(false)
                .name("orders")
                .build();

        Payload payload = Payload.builder()
                .orderId(orderDto.getOrderId())
                .userId(orderDto.getUserId())
                .productId(orderDto.getProductId())
                .qty(orderDto.getQty())
                .unitPrice(orderDto.getUnitPrice())
                .totalPrice(orderDto.getTotalPrice())
                .build();

        return new KafkaOrderDto(schema, payload);
    }
}
