package me.jincrates.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Field {
    private String type;
    private boolean optional;
    private String field;
}
