package com.example.spacelab.service;

import com.example.spacelab.dto.student.StudentCardDTO;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface StudentCardService {

    StudentCardDTO getCard(Long id);

}
