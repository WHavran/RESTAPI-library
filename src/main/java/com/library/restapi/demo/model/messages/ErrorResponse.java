package com.library.restapi.demo.model.messages;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp,
                            int status,
                            String error) {
}
