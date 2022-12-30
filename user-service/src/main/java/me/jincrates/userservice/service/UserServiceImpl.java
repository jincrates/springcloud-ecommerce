package me.jincrates.userservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.userservice.client.OrderServiceClient;
import me.jincrates.userservice.controller.response.ResponseOrder;
import me.jincrates.userservice.dto.UserDto;
import me.jincrates.userservice.jpa.UserEntity;
import me.jincrates.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Environment env;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
//    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final OrderServiceClient orderServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//        List<ResponseOrder> orders = new ArrayList<>();

        /* Using as Rest Template */
//        String orderUrl = String.format(env.getProperty("order-service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                        new ParameterizedTypeReference<List<ResponseOrder>>() {
//                });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        /* Using as Feign Client */
        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return modelMapper.map(userEntity, UserDto.class);
    }
}
