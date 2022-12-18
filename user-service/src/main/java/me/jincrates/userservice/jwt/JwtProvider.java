package me.jincrates.userservice.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final Environment env;

    public String generateToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + env.getProperty("jwt.expire_time"));

        byte[] keyBytes = Base64.getUrlEncoder().encode(env.getProperty("jwt.secret").getBytes());
        Key secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                //.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("test") // 토큰 발급자(iss)
                .setIssuedAt(now) // 발급시간(iat)
                .setExpiration(expiration) // 만료시간(exp)
                .setSubject(subject)  // 토큰 제목(subject)
                .signWith(secretKey, SignatureAlgorithm.HS512) //시크릿 키, 알고리즘
                .compact();
    }

}
