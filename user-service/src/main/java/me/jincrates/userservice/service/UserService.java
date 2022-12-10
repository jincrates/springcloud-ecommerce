package me.jincrates.userservice.service;

import me.jincrates.userservice.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
}
