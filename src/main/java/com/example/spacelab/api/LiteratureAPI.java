package com.example.spacelab.api;

import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.literature.LiteratureCardDTO;
import com.example.spacelab.dto.literature.LiteratureInfoDTO;
import com.example.spacelab.dto.literature.LiteratureListDTO;
import com.example.spacelab.dto.literature.LiteratureSaveDTO;
import com.example.spacelab.util.FilterForm;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Tag(name="Literature", description = "Literature API")
public interface LiteratureAPI {

    @Operation(
            summary = "Get Literature Page",
            description = "Get list of literature paginated by page/size params"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    ResponseEntity<?> getLiterature(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                    @RequestParam(required = false, defaultValue = "true") Boolean verified,
                                    @RequestParam(required = false, defaultValue = "0") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size);

    @Operation(
            summary = "Get Literature By Id",
            description = "Get literature by id"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LiteratureInfoDTO.class)))
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    ResponseEntity<?> getLiteratureById(@Parameter(example = "1") Long id);

    @Operation(
            summary = "Verify Literature",
            description = "Change literature's status to 'verified' by ID"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    ResponseEntity<?> verifyLiterature(@Parameter(example = "1") Long id);

    @Operation(
            summary = "Create New Literature",
            description = "Create new literature"
    )
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = LiteratureListDTO.class)))
    @PreAuthorize("!hasAuthority('literatures.write.NO_ACCESS')")
    ResponseEntity<?> createNewLiterature(@RequestBody LiteratureSaveDTO dto,
                                          BindingResult bindingResult);

    @Operation(
            summary = "Download Literature Resource",
            description = "Download literature resource file from server"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    ResponseEntity<?> downloadLiteratureResource(Long id) throws IOException;

    @Operation(
            summary = "Get Literature Edit DTO",
            description = "Get Literature DTO for edit"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = LiteratureCardDTO.class)))
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    ResponseEntity<?> getLiteratureForUpdate(@Parameter(example = "1") Long id);

    @Operation(
            summary = "Update Literature",
            description = "Update literature"
    )
    @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = LiteratureListDTO.class)))
    @PreAuthorize("!hasAuthority('literatures.edit.NO_ACCESS')")
    ResponseEntity<?> editLiterature(@Parameter(example = "1") Long id,
                                     @ModelAttribute LiteratureSaveDTO literature,
                                     BindingResult bindingResult);

    @Operation(
            summary = "Delete Literature",
            description = "Delete literature by ID"
    )
    @ApiResponse(responseCode = "200", description = "Deleted", content = @Content)
    @PreAuthorize("!hasAuthority('literatures.delete.NO_ACCESS')")
    ResponseEntity<?> deleteLiterature(@Parameter(example = "1") Long id);

    @Operation(
            summary = "Get Literature Status List",
            description = "Get literature statuses in select format: id-name, id and name are the same for statuses"
    )
    @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(array = @ArraySchema(minItems = 2, schema = @Schema(implementation = SelectDTO.class))))
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    ResponseEntity<?> getStatusList();

    @Operation(
            summary = "Get Verification Count",
            description = "Get count of literature in the verification list"
    )
    @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = Integer.class)))
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    ResponseEntity<?> getVerificationCount();

}
