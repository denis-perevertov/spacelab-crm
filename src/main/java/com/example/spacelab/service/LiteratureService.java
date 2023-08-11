package com.example.spacelab.service;

import com.example.spacelab.model.Literature;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LiteratureService {
    List<Literature> getLiterature();
    Literature getLiteratureById(Long id);
    Literature createNewLiterature(@RequestBody Literature literature);
    Literature editLiterature(@RequestBody Literature literature);
    void deleteLiteratureById(Long id);
}
