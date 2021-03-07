package com.assembly.vote.assembly.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ResponseStatusException {


    private String message;

    public UnauthorizedException(final String message) {
        super(HttpStatus.UNAUTHORIZED,  message);
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        HttpStatus status = super.getStatus();
        if (Objects.isNull(status)) {
            status = HttpStatus.UNAUTHORIZED;
        }
        return status;
    }

    @Override
    public String getReason() {
        return message;
    }
}
