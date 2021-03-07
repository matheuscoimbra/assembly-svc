package com.assembly.vote.assembly.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestException extends ResponseStatusException {


    private String message;

    public TooManyRequestException(final String message) {
        super(HttpStatus.TOO_MANY_REQUESTS,  message);
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        HttpStatus status = super.getStatus();
        if (Objects.isNull(status)) {
            status = HttpStatus.TOO_MANY_REQUESTS;
        }
        return status;
    }

    @Override
    public String getReason() {
        return message;
    }
}
