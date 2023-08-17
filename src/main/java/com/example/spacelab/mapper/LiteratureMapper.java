package com.example.spacelab.mapper;

import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.LiteratureDTO;
import lombok.Data;
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
