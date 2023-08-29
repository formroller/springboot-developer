package com.example.oauth2.config.jwt.service;

import com.example.oauth2.config.jwt.TokenProvider;
import com.example.oauth2.secur.entity.User;
import com.example.oauth2.secur.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    /*
    * 토큰 유효성 검사 진행
    * 유효한 토큰일 때, 리프레시 토큰으로 사용자 ID 찾기
    * 새로운 액세스 토큰 생성
    */

    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected Token");
        }
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId(); // 리프레시 토큰으로 사용자 id 찾기
        User user = userService.findById(userId);
        return tokenProvider.generateToken(user, Duration.ofHours(2)); // 새로운 액세스 토큰 생성
    }
}
