package com.example.spacelab.api;

import com.example.spacelab.dto.admin.AdminLoginInfoDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.model.student.StudentInviteRequest;
import com.example.spacelab.util.AuthRequest;
import com.example.spacelab.util.AuthResponse;
import com.example.spacelab.util.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name="_Auth", description = "Authorization/Authentication API")
public interface AuthAPI {

    @Operation(
            description = "Enter username & password to receive access token + refresh token",
            summary = "Login (JWT)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
    })
    ResponseEntity<?> login(@RequestBody AuthRequest authRequest);

    @Operation(
            description = "Logout & remove your authentication",
            summary = "Logout"
    )
    @ApiResponse(responseCode = "202", description = "Successfully logged out")
    ResponseEntity<?> logout();

    @Operation(
            description = "Refresh the access token by submitting your refresh token (if it's not expired)",
            summary = "Refresh Access Token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
    })
    ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refresh_token);

//    @Operation(
//            description = """
//                        Get student data for registration, extracted from token in student's invite link.
//                        If token is incorrect - registration is not possible
//                    """,
//            summary = "Get Student Data from invite link token"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentInviteRequest.class))),
//            @ApiResponse(responseCode = "404", description = "Token not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
//    })
//    ResponseEntity<?> getDataFromInviteLink(
//            @Parameter(required = true, description = "Token for extracting student data") String token
//    );

    @Operation(
            description = "Get currently logged in user's data from Authorization Header",
            summary = "Get logged in user's data"
    )
    @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminLoginInfoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity<?> getAuthData(HttpServletRequest request);

    @Hidden
    @Operation(
            description = "Confirm password of the currently logged in user, extra security measure for sensitive delete operations",
            summary = "Confirm user's password"
    )
    @ApiResponse(responseCode = "200", description = "Checked password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    ResponseEntity<?> confirmPassword(@Parameter(required = true) String password);
}
