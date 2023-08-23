package com.example.spacelab.service;

import com.example.spacelab.util.FilterForm;
import org.springframework.data.jpa.domain.Specification;

public interface EntityFilterService<T> {
    Specification<T> buildSpecificationFromFilters(FilterForm filters);
}
