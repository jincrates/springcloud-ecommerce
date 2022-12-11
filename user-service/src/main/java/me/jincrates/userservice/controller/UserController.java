package me.jincrates.userservice.controller;

import lombok.RequiredArgsConstructor;
import me.jincrates.userservice.controller.request.RequestUser;
import me.jincrates.userservice.controller.response.ResponseUser;
import me.jincrates.userservice.dto.UserDto;
import me.jincrates.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("/heath-check")
    public String status() {
        return "It's Working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser req) {
        UserDto userDto = modelMapper.map(req, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseUser);
    }

}
