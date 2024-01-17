package com.example.spacelab.integration.data;

import java.util.List;

public record ErrorResponse(
        List<Object> errors
) {
}
