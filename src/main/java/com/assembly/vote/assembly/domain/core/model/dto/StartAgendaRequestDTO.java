package com.assembly.vote.assembly.domain.core.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class StartAgendaRequestDTO {
    private String agendaId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime durationAgenda;
}
