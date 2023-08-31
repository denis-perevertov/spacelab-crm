package com.example.spacelab.controller;

import com.example.spacelab.dto.course.CourseCardDTO;
import com.example.spacelab.dto.course.CourseSelectDTO;
import com.example.spacelab.dto.literature.*;
import com.example.spacelab.dto.student.StudentDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.LiteratureMapper;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.service.LiteratureService;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.LiteratureValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name="Literature", description = "Literature controller")
@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/api/literature")
public class LiteratureController {

    private final LiteratureService literatureService;
    private final LiteratureMapper mapper;
    private final LiteratureValidator validator;



    // Получение списка литературы с фильтрацией и пагинацией
    @Operation(description = "Get Page<LiteratureListDTO>", summary = "Get Literature List", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('literature.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<Page<LiteratureListDTO>> getLiterature(FilterForm filters,
                                                                 @RequestParam(required = false) Integer page,
                                                                 @RequestParam(required = false) Integer size) {
        Page<Literature> litList = literatureService.getLiterature(filters, PageRequest.of(page, size));
        Page<LiteratureListDTO> dtoList = mapper.fromPageLiteraturetoPageDTOList(litList);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }



    // Получение литературы по id
    @Operation(description = "Get LiteratureInfoDTO by id>", summary = "Get LiteratureInfoDTO", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('literature.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<LiteratureInfoDTO> getLiteratureById(@PathVariable Long id) {
        LiteratureInfoDTO lit = mapper.fromLiteraturetoInfoDTO(literatureService.getLiteratureById(id));
        return new ResponseEntity<>(lit, HttpStatus.OK);
    }



    // Верификация литературы
    @Operation(description = "Verify Literature by id", summary = "Verify Literature", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('literature.read.NO_ACCESS')")
    @GetMapping("/{id}/verify")
    public ResponseEntity<String> verifyLiterature(@PathVariable Long id) {
        literatureService.verifyLiterature(id);
        return new ResponseEntity<>("Verified successfully!", HttpStatus.OK);
    }



    // Создание новой литературы
    @Operation(description = "Create new Literature", summary = "Create Literature", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid Literature object", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('literature.wrire.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<String> createNewLiterature(@Valid @RequestBody LiteratureSaveDTO literature, BindingResult bindingResult) {
        validator.validate(literature, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        literatureService.createNewLiterature(mapper.fromLiteratureSaveDTOtoLiterature(literature));
        return new ResponseEntity<>("Successful save", HttpStatus.CREATED);
    }



    // Получение литературы для редактирования по id
    @Operation(description = "Get Literature by id for edit", summary = "Get Literature by id for edit", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('literature.read.NO_ACCESS')")
    @GetMapping("/update/{id}")
    private ResponseEntity<LiteratureCardDTO> getCourseForUpdate(@PathVariable Long id) {
        LiteratureCardDTO literatureCardDTO = mapper.fromLiteratureToCardDTO(literatureService.getLiteratureById(id));
        return new ResponseEntity<>(literatureCardDTO, HttpStatus.OK);
    }



    // Редактирование литературы
    @Operation(description = "Edit Literature by id", summary = "Edit Literature", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid Literature object", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('literature.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<String> editLiterature(@PathVariable Long id, @Valid @RequestBody LiteratureSaveDTO literature, BindingResult bindingResult) {
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
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Literature not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('literature.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLiterature(@PathVariable Long id) {
        literatureService.deleteLiteratureById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }



    // Select2
    @Operation(description = "Get Literature for select2", summary = "Get Literature for select2", tags = {"Literature"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('literature.read.NO_ACCESS')")
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
}
