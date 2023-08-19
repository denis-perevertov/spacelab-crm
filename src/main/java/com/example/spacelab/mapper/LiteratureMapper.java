package com.example.spacelab.mapper;

import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.LiteratureDTO.LiteratureListDTO;
import org.springframework.stereotype.Component;

@Component
public class LiteratureMapper {

    public LiteratureListDTO fromLiteratureToDTO(Literature literature) {

        return new LiteratureListDTO();
    }

    public Literature fromDTOToLiterature(LiteratureListDTO dto) {

        return new Literature();
    }
}
