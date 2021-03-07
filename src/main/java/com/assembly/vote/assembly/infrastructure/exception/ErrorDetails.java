package com.assembly.vote.assembly.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class ErrorDetails {
    private long timestamp;
    private String message;
    private String details;


}