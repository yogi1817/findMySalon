package com.spj.salon.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode()
public class NotFoundCustomException extends RuntimeException {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -1256873401088084366L;

    private String message;
    private String details;

    public NotFoundCustomException(String message, String details) {
        super();
        this.message = message;
        this.details = details;
    }

    public NotFoundCustomException() {

    }
}
