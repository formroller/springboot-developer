package com.example.security.jwt;

import com.example.security.config.jwt.TokenProvider;
import com.example.security.secur.entity.User;
import com.example.security.secur.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TokenProvideTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;


    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret_key}")
    private String secretKey;

    // 1. generateToken() 검증 테스트
    @DisplayName("generateToken() : 유저 정보 및 만료 기간 전달해 토큰 생성")
    @Test
    void generateToken() {
        // given
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then
        Long userId = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    // 2-1.validToken() 검증 테스트
    @DisplayName("validToken() : 만료된 토큰은 검증 실패")
    @Test
    void validToken_invalidToken() {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(issuer, secretKey);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isFalse();
    }


    // 2-2. validToken() 검증 테스트
    @DisplayName("validToken : 유효 토큰 검증 성공")
    @Test
    void validToken_validToken(){
        // given
        String token = JwtFactory.builder().build().createToken(issuer,secretKey);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }

    // 3.getAuthentication() 검증
    @DisplayName("getAuthentication() 토큰 기반 인증 정보 가져오기")
    @Test
    void getAuthentication(){
        // given
        String userEmail = "aa@aa.com";

        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(issuer, secretKey);

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails)authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    // 4. getUserId() 검증 테스트
    @DisplayName("getUserId(), 토큰으로 유저 ID 가져올 수 있음")
    @Test
    void getUserId(){
        // given
        Long userId = 1L;

        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(issuer, secretKey);

        // when
        Long userIdByToken = tokenProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}