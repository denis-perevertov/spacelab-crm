package com.example.spacelab.api;

import com.example.spacelab.dto.settings.SettingsDTO;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.util.EnumResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Settings", description = "Settings API")
public interface SettingsAPI {

    @Operation(
            summary = "Get Settings For Logged In Admin"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = SettingsDTO.class)))
    ResponseEntity<?> getLoggedInAdminSettings(@Parameter(hidden = true) Admin admin);

    @Operation(
            summary = "Get Theme Settings Options"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(minItems = 2, schema = @Schema(implementation = EnumResponse.class))))
    ResponseEntity<?> getAllThemeOptions();

    @Operation(
            summary = "Get Language Settings Options"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(minItems = 2, schema = @Schema(implementation = EnumResponse.class))))
    ResponseEntity<?> getAllLanguageOptions();

    @Operation(
            summary = "Get Hours Norm Settings Options",
            description = "Get options for possible studying hours norms"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(minItems = 2, schema = @Schema(implementation = EnumResponse.class))))
    ResponseEntity<?> getAllHoursNormValues();

    @Operation(
            summary = "Get Interval Settings Options",
            description = "Get options for possible intervals between lessons"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(minItems = 2, schema = @Schema(implementation = EnumResponse.class))))
    ResponseEntity<?> getAllIntervalValues();

    @Operation(
            summary = "Save Settings For Logged In Admin"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = SettingsDTO.class)))
    ResponseEntity<?> saveSettingsForLoggedInAdmin(@Parameter(hidden = true) Admin admin,
                                                   @RequestBody SettingsDTO dto);
}
