package com.example.spacelab.service;

import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.LiteratureDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LiteratureService extends EntityService<Literature>{
    List<LiteratureDTO> getLiterature();
    List<LiteratureDTO> getLiterature(Pageable pageable);
    List<LiteratureDTO> getLiterature(FilterForm filters, Pageable pageable);
    LiteratureDTO getLiteratureById(Long id);
    LiteratureDTO createNewLiterature(LiteratureDTO literature);
    LiteratureDTO editLiterature(LiteratureDTO literature);

    void verifyLiterature(Long id);

    void deleteLiteratureById(Long id);
}