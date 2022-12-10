package me.jincrates.userservice.controller;

import lombok.RequiredArgsConstructor;
import me.jincrates.userservice.controller.request.RequestUser;
import me.jincrates.userservice.dto.UserDto;
import me.jincrates.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
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
    public String createUser(@RequestBody RequestUser req) {
        UserDto userDto = modelMapper.map(req, UserDto.class);
        userService.createUser(userDto);

        return "Create user method is called";
    }

}
