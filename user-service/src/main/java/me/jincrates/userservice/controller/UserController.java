package me.jincrates.userservice.controller;

import lombok.RequiredArgsConstructor;
import me.jincrates.userservice.controller.request.RequestUser;
import me.jincrates.userservice.controller.response.ResponseUser;
import me.jincrates.userservice.dto.UserDto;
import me.jincrates.userservice.jpa.UserEntity;
import me.jincrates.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("/health")
    public String status() {
        return String.format("It's Working in User Service on PORT %s",
                env.getProperty("local.server.port"));
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

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> users = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        users.forEach(u -> {
            result.add(modelMapper.map(u, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
}
