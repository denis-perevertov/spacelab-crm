package com.example.spacelab.service.impl;

import com.example.spacelab.model.Literature;
import com.example.spacelab.repository.LiteratureRepository;
import com.example.spacelab.service.LiteratureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class LiteratureServiceImpl implements LiteratureService{

    private final LiteratureRepository literatureRepository;

    @Override
    public List<Literature> getLiterature() {
        return null;
    }

    @Override
    public Literature getLiteratureById(Long id) {
        return null;
    }

    @Override
    public Literature createNewLiterature(Literature literature) {
        return null;
    }

    @Override
    public Literature editLiterature(Literature literature) {
        return null;
    }

    @Override
    public void deleteLiteratureById(Long id) {

    }
}
