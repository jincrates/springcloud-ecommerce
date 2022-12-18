package me.jincrates.userservice.dto;

import lombok.Data;
import me.jincrates.userservice.controller.response.ResponseOrder;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String userId;
    private LocalDateTime createdAt;

    private String encryptedPassword;

    private List<ResponseOrder> orders;
}
