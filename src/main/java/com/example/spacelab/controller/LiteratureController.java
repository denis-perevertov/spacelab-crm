package com.example.spacelab.controller;

import com.example.spacelab.model.Literature;
import com.example.spacelab.service.LiteratureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    public List<Literature> getLiterature() {
        return literatureService.getLiterature();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Literature> getLiteratureById(@PathVariable Long id) {
        Literature lit = literatureService.getLiteratureById(id);
        return new ResponseEntity<>(lit, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<String> createNewLiterature(@RequestBody Literature literature) {
        literatureService.createNewLiterature(literature);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> editLiterature(@RequestBody Literature literature) {
        literatureService.editLiterature(literature);
        return new ResponseEntity<>("Edited", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLiterature(@PathVariable Long id) {
        literatureService.deleteLiteratureById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
