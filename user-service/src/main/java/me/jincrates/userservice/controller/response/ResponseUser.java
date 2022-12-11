package me.jincrates.userservice.controller.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResponseUser {
    private String email;
    private String name;
    private String userId;
}
