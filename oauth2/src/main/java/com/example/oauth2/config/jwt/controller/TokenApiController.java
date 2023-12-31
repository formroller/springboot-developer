package com.example.oauth2.config.jwt.controller;

import com.example.oauth2.config.jwt.dto.CreateAccessTokenRequest;
import com.example.oauth2.config.jwt.dto.CreateAccessTokenResponse;
import com.example.oauth2.config.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return new ResponseEntity<>(new CreateAccessTokenResponse(newAccessToken), HttpStatus.CREATED);
    }
}
