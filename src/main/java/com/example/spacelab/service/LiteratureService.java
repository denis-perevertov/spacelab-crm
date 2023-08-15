package com.example.spacelab.service;

import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.LiteratureDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LiteratureService {
    List<Literature> getLiterature();
    List<LiteratureDTO> getLiteratureByPage(Pageable pageable);
    Literature getLiteratureById(Long id);
    Literature createNewLiterature(Literature literature);
    Literature createNewLiterature(LiteratureDTO literature);
    Literature editLiterature(Literature literature);
    Literature editLiterature(LiteratureDTO literature);
    void deleteLiteratureById(Long id);
}
