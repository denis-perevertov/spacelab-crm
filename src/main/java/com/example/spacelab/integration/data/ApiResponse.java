package com.example.spacelab.integration.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    T data;
    List<Object> errors;
    String status;

}
