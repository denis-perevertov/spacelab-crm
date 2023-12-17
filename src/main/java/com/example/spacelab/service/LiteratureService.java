package com.example.spacelab.service;

import com.example.spacelab.dto.literature.LiteratureSaveDTO;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface LiteratureService extends EntityFilterService<Literature>{
    List<Literature> getLiterature();
    Page<Literature> getLiterature(Pageable pageable);
    Page<Literature> getLiterature(FilterForm filters, Pageable pageable);

    List<Literature> getLiteratureByAllowedCourses(Long... ids);
    Page<Literature> getLiteratureByAllowedCourses(Pageable pageable, Long... ids);
    Page<Literature> getLiteratureByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids);

    Literature getLiteratureById(Long id);
    Literature createNewLiterature(Literature literature);
    Literature createNewLiterature(LiteratureSaveDTO saveRequest) throws IOException;
    Literature editLiterature(LiteratureSaveDTO saveRequest) throws IOException;

    File getLiteratureFileById(Long id) throws IOException;

    Page<Literature> getLiteratureByName(String name, Pageable pageable);

    void verifyLiterature(Long id);

    void deleteLiteratureById(Long id);
}
