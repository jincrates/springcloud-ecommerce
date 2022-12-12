package me.jincrates.userservice.service;

import me.jincrates.userservice.dto.UserDto;
import me.jincrates.userservice.jpa.UserEntity;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);

    Iterable<UserEntity> getUserByAll();
}
