package com.example.spacelab.service;

import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.LiteratureDTO.LiteratureListDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LiteratureService extends EntityService<Literature>{
    List<LiteratureListDTO> getLiterature();
    List<LiteratureListDTO> getLiterature(Pageable pageable);
    List<LiteratureListDTO> getLiterature(FilterForm filters, Pageable pageable);
    LiteratureListDTO getLiteratureById(Long id);
    LiteratureListDTO createNewLiterature(LiteratureListDTO literature);
    LiteratureListDTO editLiterature(LiteratureListDTO literature);

    void verifyLiterature(Long id);

    void deleteLiteratureById(Long id);
}
