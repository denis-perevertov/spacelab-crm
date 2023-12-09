package com.example.spacelab.controller;

import com.example.spacelab.dto.course.CourseCardDTO;
import com.example.spacelab.dto.course.CourseSelectDTO;
import com.example.spacelab.dto.literature.*;
import com.example.spacelab.dto.student.StudentDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.LiteratureMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.literature.LiteratureType;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.student.StudentAccountStatus;
import com.example.spacelab.service.LiteratureService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.LiteratureValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="Literature", description = "Literature controller")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/literature")
public class LiteratureController {

    private final LiteratureService literatureService;
    private final LiteratureMapper mapper;
    private final LiteratureValidator validator;

    private final AuthUtil authUtil;



    // Получение списка литературы с фильтрацией и пагинацией
    @Operation(description = "Get list of literature paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)", summary = "Get Literature List", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<Page<LiteratureListDTO>> getLiterature(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                                                 @RequestParam(required = false, defaultValue = "true") Boolean verified,
                                                                 @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<LiteratureListDTO> literatures;
        Page<Literature> litPage = new PageImpl<>(new ArrayList<>());
        Pageable pageable = PageRequest.of(page, size);
        filters.setVerified(verified);

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadStudents();
        List<Course> adminCourses = loggedInAdmin.getCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            litPage = literatureService.getLiterature(filters, pageable);
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = (Long[]) adminCourses.stream().map(Course::getId).toArray();
            litPage = literatureService.getLiteratureByAllowedCourses(filters, pageable, allowedCoursesIDs);
        }
        literatures = new PageImpl<>(litPage.getContent().stream().map(mapper::fromLiteratureToListDTO).toList(), pageable, litPage.getTotalElements());

        return new ResponseEntity<>(literatures, HttpStatus.OK);
    }



    // Получение литературы по id
    @Operation(description = "Get LiteratureInfoDTO by id>", summary = "Get LiteratureInfoDTO", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<LiteratureInfoDTO> getLiteratureById(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.read");

        LiteratureInfoDTO lit = mapper.fromLiteraturetoInfoDTO(literatureService.getLiteratureById(id));
        return new ResponseEntity<>(lit, HttpStatus.OK);
    }



    // Верификация литературы
    @Operation(description = "Verify Literature by id", summary = "Verify Literature", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    @GetMapping("/{id}/verify")
    public ResponseEntity<String> verifyLiterature(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.verify");

        literatureService.verifyLiterature(id);
        return new ResponseEntity<>("Verified successfully!", HttpStatus.OK);
    }



    // Создание новой литературы
    @Operation(description = "Create new Literature; ID field does not matter in write/edit operations", summary = "Create Literature", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid Literature object", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('literatures.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<?> createNewLiterature( @ModelAttribute LiteratureSaveDTO dto, BindingResult bindingResult) {

        authUtil.checkAccessToCourse(dto.getCourseID(), "literatures.write");

        dto.setId(null);

        validator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        try {
            Literature createdLiterature = literatureService.createNewLiterature(dto);
            return new ResponseEntity<>(mapper.fromLiteratureToListDTO(createdLiterature), HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("Exception with saving lit file: " + e.getMessage());
            return ResponseEntity.badRequest().body("Exception with saving file!");
        } catch (Exception e) {
            log.error("Unknown exception during lit creation: " + e.getMessage());
            return ResponseEntity.badRequest().body("Could not save literature!");
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadLiteratureResource(@PathVariable Long id,
                                           HttpServletResponse response) throws IOException {
        File file = literatureService.getLiteratureFileById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(file.getName(), file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        log.info("Finished with response");
        return new ResponseEntity<>(new InputStreamResource(new FileInputStream(file)), headers, HttpStatus.OK);
    }

    // Получение литературы для редактирования по id
    @Operation(description = "Get Literature by id for edit", summary = "Get Literature by id for edit", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    @GetMapping("/update/{id}")
    public ResponseEntity<LiteratureCardDTO> getCourseForUpdate(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.read");

        LiteratureCardDTO literatureCardDTO = mapper.fromLiteratureToCardDTO(literatureService.getLiteratureById(id));
        return new ResponseEntity<>(literatureCardDTO, HttpStatus.OK);
    }


    // Редактирование литературы
    @Operation(description = "Edit Literature by id; ID field does not matter in write/edit operations", summary = "Edit Literature", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('literatures.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<String> editLiterature(@PathVariable @Parameter(example = "1") Long id,  @RequestBody LiteratureSaveDTO literature, BindingResult bindingResult) {

        // проверка и для того курса , куда пихаешь источник , и для того курса , который у источника был раньше

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.edit");
        authUtil.checkAccessToCourse(literature.getCourseID(), "literatures.edit");

        literature.setId(id);

        validator.validate(literature, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        literatureService.editLiterature(mapper.fromLiteratureSaveDTOtoLiterature(literature));
        return new ResponseEntity<>("Successful update", HttpStatus.OK);
    }



    // Удаление литературы
    @Operation(description = "Delete Literature by id", summary = "Delete Literature", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('literatures.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLiterature(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.delete");

        literatureService.deleteLiteratureById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }



    // Select2
    @Operation(description = "Get Literature for select2", summary = "Get Literature for select2", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('literatures.read.NO_ACCESS')")
    @GetMapping("/getLiteratures")
    @ResponseBody
    public Page<LiteratureSelectDTO> getOwners(@RequestParam(name = "searchQuery", defaultValue = "") String searchQuery,
                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Literature> literatures = literatureService.getLiteratureByName(searchQuery, pageable);
        Page<LiteratureSelectDTO> ownerPage = literatures.map(mapper::fromLiteratureToSelectDTO);
        return ownerPage;
    }

    // Получение списка типов литературы
    @GetMapping("/get-literature-type-list")
    public List<LiteratureType> getStatusList() {
        return List.of(LiteratureType.values());
    }

}
