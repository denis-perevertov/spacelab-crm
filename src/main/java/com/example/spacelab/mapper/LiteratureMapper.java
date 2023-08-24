package com.example.spacelab.mapper;

import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.dto.LiteratureDTO;
import org.springframework.stereotype.Component;

@Component
public class LiteratureMapper {

    public LiteratureDTO fromLiteratureToDTO(Literature literature) {

        return new LiteratureDTO();
    }

    public Literature fromDTOToLiterature(LiteratureDTO dto) {

        return new Literature();
    }
}
