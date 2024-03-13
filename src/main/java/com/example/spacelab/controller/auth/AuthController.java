package com.example.spacelab.controller.auth;

import com.example.spacelab.api.AuthAPI;
import com.example.spacelab.config.JwtService;
import com.example.spacelab.dto.admin.AdminLoginInfoDTO;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.exception.TokenException;
import com.example.spacelab.mapper.AdminMapper;
import com.example.spacelab.model.RefreshToken;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.repository.InviteStudentRequestRepository;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.service.RefreshTokenService;
import com.example.spacelab.util.AuthRequest;
import com.example.spacelab.util.AuthResponse;
import com.example.spacelab.util.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthAPI {

    private final JwtService jwtService;
    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    private final InviteStudentRequestRepository inviteStudentRequestRepository;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if(authentication.isAuthenticated()) {
                String access_token = jwtService.generateToken(authRequest.username());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.username());
                return ResponseEntity.ok(new AuthResponse(access_token, refreshToken.getToken()));
            }
            else throw new ResourceNotFoundException("Incorrect authentication request");
        } catch (BadCredentialsException ex) {
            throw new ResourceNotFoundException("Incorrect authentication request");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refresh_token) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refresh_token.refresh_token());
        if(refreshToken.getExpiryDate().isAfter(Instant.now())) {
            String newAccessToken = jwtService.generateToken(adminService.getAdminById(refreshToken.getPrincipal().getId()).getEmail());
            return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken.getToken()));
        }
        else throw new TokenException("Refresh token expired!");
    }

//    @PostMapping("/invite-data")
//    public ResponseEntity<?> getDataFromInviteLink(@RequestParam String token) {
//        return ResponseEntity.ok(inviteStudentRequestRepository.findById(token)
//                .orElseThrow(() -> new EntityNotFoundException("Incorrect token!")));
//    }

    @GetMapping("/me")
    public ResponseEntity<?> getAuthData(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String adminEmail = jwtService.extractUsername(token);
        return ResponseEntity.ok(adminMapper.fromAdminToLoginInfoDTO(adminService.getAdminByEmail(adminEmail)));
    }

    @PostMapping("/confirm-password")
    @ResponseBody
    public ResponseEntity<?> confirmPassword(@RequestBody String password) {
        Admin loggedInAdmin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(encoder.matches(password.substring(1, password.length()-1), loggedInAdmin.getPassword()));
    }

}
