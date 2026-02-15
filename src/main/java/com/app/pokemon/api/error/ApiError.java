package com.app.pokemon.api.error;

import java.time.OffsetDateTime;

public record ApiError(
        String code,
        String message,
        String path,
        String requestId,
        OffsetDateTime timestamp
) {}
