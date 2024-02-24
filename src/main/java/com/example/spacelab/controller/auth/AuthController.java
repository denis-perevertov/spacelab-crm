package com.example.spacelab.controller.auth;

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

@Tag(name="_Auth", description = "Login, Logout, Refresh Token")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    private final InviteStudentRequestRepository inviteStudentRequestRepository;

    @Operation(description = "Enter username & password to receive access token + refresh token", summary = "Login (JWT)", tags = {"_Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
    })
    @PostMapping(value = "/login", consumes = "application/json")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if(authentication.isAuthenticated()) {
                String access_token = jwtService.generateToken(authRequest.username());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.username());
                return new AuthResponse(access_token, refreshToken.getToken());
            }
            else throw new ResourceNotFoundException("Incorrect authentication request");
        } catch (BadCredentialsException ex) {
            throw new ResourceNotFoundException("Incorrect authentication request");
        }
    }

    @Operation(description = "Logout & remove your authentication", summary = "Logout", tags = {"_Auth"})
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "Logged out";
    }

    @Operation(description = "Refresh the access token by submitting your refresh token (if it's not expired)",
            summary = "Refresh Access Token", tags = {"_Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
    })
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest refresh_token) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refresh_token.refresh_token());
        if(refreshToken.getExpiryDate().isAfter(Instant.now())) {
            String newAccessToken = jwtService.generateToken(refreshToken.getAdmin().getEmail());
            return new AuthResponse(newAccessToken, refreshToken.getToken());
        }
        else throw new TokenException("Refresh token expired!");
    }

    @PostMapping("/invite-data")
    public ResponseEntity<?> getDataFromInviteLink(@RequestParam String token) {
        return ResponseEntity.ok(inviteStudentRequestRepository.findById(token)
                .orElseThrow(() -> new EntityNotFoundException("Incorrect token!")));
    }

    @GetMapping("/me")
    public AdminLoginInfoDTO getAuthData(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String adminEmail = jwtService.extractUsername(token);
        return adminMapper.fromAdminToLoginInfoDTO(adminService.getAdminByEmail(adminEmail));
    }

    @PostMapping("/confirm-password")
    @ResponseBody
    public boolean confirmPassword(@RequestBody String password) {
        Admin loggedInAdmin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return encoder.matches(password.substring(1, password.length()-1), loggedInAdmin.getPassword());
    }

}
