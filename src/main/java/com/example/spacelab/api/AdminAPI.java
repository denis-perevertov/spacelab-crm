package com.example.spacelab.api;

import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.admin.AdminAvatarDTO;
import com.example.spacelab.dto.admin.AdminEditDTO;
import com.example.spacelab.dto.course.CourseAdminIconDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name="Admin", description = "Admin API")
public interface AdminAPI {

    @Operation(description = "Get list of admins paginated by 'page/size' params (default values are 0/10)", summary = "Get Admins (page)", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    ResponseEntity<?> getAdmins(@Parameter(name = "Filter object", description = "Collection of all filters for search results") FilterForm filters,
                                @Parameter(example = "0") Integer page,
                                @Parameter(example = "10") Integer size);

    @Operation(description = "Get admin info by his ID: Name, Phone, Email, Role & Courses", summary = "Get Admin DTO By ID", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    ResponseEntity<?> getAdmin(@Parameter(example = "1") Long id);

    @Operation(
            description = "Get courses of admin with specified ID",
            summary = "Get Admin Courses",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseAdminIconDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    ResponseEntity<?> getAdminCourses(@Parameter(example = "1", required = true) Long id);

    @Operation(description = "Create new admin in the application; ID field does not matter in write/edit operations", summary = "Create New Admin", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Creation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.write.NO_ACCESS')")
    ResponseEntity<?> createNewAdmin(@RequestBody AdminEditDTO admin,
                                     BindingResult bindingResult) throws IOException;

    @Operation(
            description = "Get admin's information for editing",
            summary = "Get Admin Info For Edit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdminEditDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    ResponseEntity<?> getAdminForEdit(@Parameter(example = "1", required = true) Long id);

    @Operation(description = "Update existing admin in the application; ID field does not matter in write/edit operations", summary = "Update Admin", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Update"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.edit.NO_ACCESS')")
    ResponseEntity<?> updateAdmin(@Parameter(example = "1", required = true) Long id,
                                  @RequestBody AdminEditDTO admin,
                                  BindingResult bindingResult) throws IOException;

    @Operation(description = "Delete admin by his ID", summary = "Delete Admin By ID", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.delete.NO_ACCESS')")
    ResponseEntity<?> deleteAdmin(@Parameter(example = "1", required = true) Long id);

    @Operation(
            description = "Upload admin's avatar",
            summary = "Upload Avatar",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully uploaded"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("!hasAuthority('settings.edit.NO_ACCESS')")
    ResponseEntity<?> uploadAvatar(@Parameter(example = "1", required = true) Long id,
                                   MultipartFile avatar) throws IOException;

    @Operation(
            description = "Delete admin's avatar",
            summary = "Delete Avatar",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("!hasAuthority('settings.edit.NO_ACCESS')")
    ResponseEntity<?> deleteAvatar(Long id);

    @Operation(
            description = "Get list of admins in select format: id-name",
            summary = "Get Admin List For Select"
    )
    @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SelectDTO.class)))
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    ResponseEntity<?> getAdminSelect();

    @Operation(
            description = "Get list of admins with specified Role ID",
            summary = "Get Admin List By Role"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdminAvatarDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    ResponseEntity<?> getAdminsListByRole(@Parameter(example = "1", required = true) Long roleID);

    @Operation(description = "Get list of admins not attached to any courses", summary = "Get Available Admins", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    ResponseEntity<?> getAdminsWithoutCourses(FilterForm filters,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size);

}
