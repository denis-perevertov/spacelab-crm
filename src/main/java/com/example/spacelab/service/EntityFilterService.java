package com.example.spacelab.service;

import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.domain.Specification;
@Hidden
public interface EntityFilterService<T> {
    Specification<T> buildSpecificationFromFilters(FilterForm filters);
}
