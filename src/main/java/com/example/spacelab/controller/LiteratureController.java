package com.example.spacelab.controller;

import com.example.spacelab.api.LiteratureAPI;
import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.literature.*;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.LiteratureMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.literature.LiteratureType;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.service.FileService;
import com.example.spacelab.service.LiteratureService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.LiteratureValidator;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/literature")
public class LiteratureController implements LiteratureAPI {

    private final LiteratureService literatureService;
    private final LiteratureMapper mapper;
    private final LiteratureValidator validator;

    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<?> getLiterature(FilterForm filters,
                                           @RequestParam(required = false, defaultValue = "true") Boolean verified,
                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<LiteratureListDTO> literatures;
        Page<Literature> litPage = new PageImpl<>(new ArrayList<>());
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        filters.setVerified(verified);

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadStudents();
        Set<Course> adminCourses = loggedInAdmin.getCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            litPage = literatureService.getLiterature(filters.trim(), pageable);
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = adminCourses.stream().map(Course::getId).toArray(Long[]::new);
            litPage = literatureService.getLiteratureByAllowedCourses(filters.trim(), pageable, allowedCoursesIDs);
        }
        literatures = new PageImpl<>(litPage.getContent().stream().map(mapper::fromLiteratureToListDTO).toList(), pageable, litPage.getTotalElements());

        return new ResponseEntity<>(literatures, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLiteratureById(@PathVariable Long id) {

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.read");
        LiteratureInfoDTO dto = mapper.fromLiteraturetoInfoDTO(literatureService.getLiteratureById(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<?> verifyLiterature(@PathVariable Long id) {

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.verify");

        literatureService.verifyLiterature(id);
        return new ResponseEntity<>("Verified successfully!", HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createNewLiterature( @ModelAttribute LiteratureSaveDTO dto,
                                                  BindingResult bindingResult) {

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
    public ResponseEntity<?> downloadLiteratureResource(@PathVariable Long id) throws IOException {
        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.read");
        File file = literatureService.getLiteratureFileById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(file.getName(), file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        log.info("Finished with response");
        return new ResponseEntity<>(new InputStreamResource(new FileInputStream(file)), headers, HttpStatus.OK);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> getLiteratureForUpdate(@PathVariable Long id) {

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.read");

        LiteratureCardDTO literatureCardDTO = mapper.fromLiteratureToCardDTO(literatureService.getLiteratureById(id));
        return new ResponseEntity<>(literatureCardDTO, HttpStatus.OK);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editLiterature(@PathVariable Long id,
                                            @ModelAttribute LiteratureSaveDTO literature,
                                            BindingResult bindingResult) {

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

        try {
            Literature editedLiterature = literatureService.editLiterature(literature);
            return new ResponseEntity<>(mapper.fromLiteratureToListDTO(editedLiterature), HttpStatus.OK);
        } catch (IOException e) {
            log.error("Exception with saving lit file: " + e.getMessage());
            return ResponseEntity.badRequest().body("Exception with saving file!");
        } catch (Exception e) {
            log.error("Unknown exception during lit creation: " + e.getMessage());
            return ResponseEntity.badRequest().body("Could not save literature!");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLiterature(@PathVariable Long id) {

        authUtil.checkAccessToCourse(literatureService.getLiteratureById(id).getCourse().getId(), "literatures.delete");

        literatureService.deleteLiteratureById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping("/get-literature-type-list")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(
                Arrays.stream(LiteratureType.values()).map(type -> new SelectDTO(type.name(), type.name())).toList()
        );
    }

    @GetMapping("/verification-count")
    public ResponseEntity<?> getVerificationCount() {
        return ResponseEntity.ok(literatureService.getVerificationCount());
    }

}
