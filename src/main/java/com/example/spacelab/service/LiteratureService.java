package com.example.spacelab.service;

import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LiteratureService extends EntityFilterService<Literature>{
    List<Literature> getLiterature();
    Page<Literature> getLiterature(Pageable pageable);
    Page<Literature> getLiterature(FilterForm filters, Pageable pageable);
    Literature getLiteratureById(Long id);
    Literature createNewLiterature(Literature literature);
    Literature editLiterature(Literature literature);

    Page<Literature> getLiteratureByName(String name, Pageable pageable);

    void verifyLiterature(Long id);

    void deleteLiteratureById(Long id);
}
