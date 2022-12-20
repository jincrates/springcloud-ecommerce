package me.jincrates.userservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.userservice.controller.request.RequestLogin;
import me.jincrates.userservice.dto.UserDto;
import me.jincrates.userservice.jwt.JwtProvider;
import me.jincrates.userservice.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Environment env;
    private final UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getEmail(),
                        creds.getPassword(),
                        new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String username = ((User) authResult.getPrincipal()).getUsername();

        UserDto userDetails = userService.getUserDetailsByEmail(username);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + Long.parseLong(env.getProperty("jwt.expire_time")));

        byte[] keyBytes = Base64.getUrlEncoder().encode(env.getProperty("jwt.secret").getBytes());
        Key secretKey = Keys.hmacShaKeyFor(keyBytes);

        String token =  Jwts.builder()
                //.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("jincrates") // 토큰 발급자(iss)
                .setIssuedAt(now) // 발급시간(iat)
                .setExpiration(expiration) // 만료시간(exp)
                .setSubject(username)  // 토큰 제목(subject)
                .signWith(secretKey, SignatureAlgorithm.HS512) //시크릿 키, 알고리즘
                .compact();

        log.debug(token);

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }
}
