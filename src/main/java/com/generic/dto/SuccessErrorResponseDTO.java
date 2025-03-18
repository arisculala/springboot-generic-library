package com.generic.dto;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object for success/error response.")
public class SuccessErrorResponseDTO {
    private HttpStatus status;
    private boolean success;
    private String message;

    public SuccessErrorResponseDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
