package com.example.spacelab.controller.auth;

import com.example.spacelab.config.JwtService;
import com.example.spacelab.exception.TokenException;
import com.example.spacelab.model.RefreshToken;
import com.example.spacelab.service.RefreshTokenService;
import com.example.spacelab.util.AuthRequest;
import com.example.spacelab.util.AuthResponse;
import com.example.spacelab.util.RefreshTokenRequest;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        if(authentication.isAuthenticated()) {
            String access_token = jwtService.generateToken(authRequest.username());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.username());
            return new AuthResponse(access_token, refreshToken.getToken());
        }
        else throw new UsernameNotFoundException("Incorrect authentication request");
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "Logged out";
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest refresh_token) {
        System.out.println(refresh_token.refresh_token());
        RefreshToken refreshToken = refreshTokenService.findByToken(refresh_token.refresh_token());
        if(refreshToken.getExpiryDate().isAfter(Instant.now())) {
            String newAccessToken = jwtService.generateToken(refreshToken.getAdmin().getEmail());
            return new AuthResponse(newAccessToken, refreshToken.getToken());
        }
        else throw new TokenException("Refresh token expired!");
    }

}
