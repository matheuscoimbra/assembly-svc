package com.assembly.vote.assembly.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends ResponseStatusException {


    private String message;

    public NotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND,  message);
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        HttpStatus status = super.getStatus();
        if (Objects.isNull(status)) {
            status = HttpStatus.NOT_FOUND;
        }
        return status;
    }

    @Override
    public String getReason() {
        return message;
    }
}
