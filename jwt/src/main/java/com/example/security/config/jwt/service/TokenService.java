package com.example.security.config.jwt.service;

import com.example.security.config.jwt.TokenProvider;
import com.example.security.secur.entity.User;
import com.example.security.secur.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken){
        // 토큰 유효성 검사 실패시 예외 발생
        if(!tokenProvider.validToken(refreshToken)){ // 유효성 검사
            throw new IllegalArgumentException("Unexpected token");
        }
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId(); // 리프레시 토큰으로 사용자 id 찾기
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2)); // 새로운 액세스 토큰 생성
    }
}
