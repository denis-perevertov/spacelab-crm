package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.LiteratureDTOMapper;
import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.LiteratureDTO;
import com.example.spacelab.repository.LiteratureRepository;
import com.example.spacelab.service.LiteratureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class LiteratureServiceImpl implements LiteratureService{

    private final LiteratureRepository literatureRepository;
    private final LiteratureDTOMapper literatureMapper;

    @Override
    public List<Literature> getLiterature() {
        return literatureRepository.findAll();
    }

    @Override
    public List<LiteratureDTO> getLiteratureByPage(Pageable pageable) {
        return literatureRepository.findAll(pageable).get().map(literatureMapper::fromLiteratureToDTO).toList();
    }

    @Override
    public Literature getLiteratureById(Long id) {
        return literatureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Literature not found"));
    }

    @Override
    public Literature createNewLiterature(Literature literature) {
        return literatureRepository.save(literature);
    }

    @Override
    public Literature createNewLiterature(LiteratureDTO literature) {
        return literatureRepository.save(literatureMapper.fromDTOToLiterature(literature));
    }

    @Override
    public Literature editLiterature(Literature literature) {
        return literatureRepository.save(literature);
    }

    @Override
    public Literature editLiterature(LiteratureDTO literature) {
        return literatureRepository.save(literatureMapper.fromDTOToLiterature(literature));
    }

    @Override
    public void deleteLiteratureById(Long id) {
        literatureRepository.deleteById(id);
    }
}
