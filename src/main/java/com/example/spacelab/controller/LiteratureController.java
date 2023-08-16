package com.example.spacelab.controller;

import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.LiteratureDTO;
import com.example.spacelab.service.LiteratureService;
import com.example.spacelab.util.FilterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/literature")
public class LiteratureController {

    private final LiteratureService literatureService;

    @GetMapping
    public ResponseEntity<List<LiteratureDTO>> getLiterature(FilterForm filters,
                                                             @RequestParam Integer page,
                                                             @RequestParam Integer size) {
        List<LiteratureDTO> litList = literatureService.getLiterature(filters, PageRequest.of(page, size));
        return new ResponseEntity<>(litList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LiteratureDTO> getLiteratureById(@PathVariable Long id) {
        LiteratureDTO lit = literatureService.getLiteratureById(id);
        return new ResponseEntity<>(lit, HttpStatus.OK);
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<String> verifyLiterature(@PathVariable Long id) {
        literatureService.verifyLiterature(id);
        return new ResponseEntity<>("Verified successfully!", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LiteratureDTO> createNewLiterature(@RequestBody LiteratureDTO literature) {
        literature = literatureService.createNewLiterature(literature);
        return new ResponseEntity<>(literature, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LiteratureDTO> editLiterature(@PathVariable Long id, @RequestBody LiteratureDTO literature) {
        literature = literatureService.editLiterature(literature);
        return new ResponseEntity<>(literature, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLiterature(@PathVariable Long id) {
        literatureService.deleteLiteratureById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
