package com.assembly.vote.assembly.domain.core.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class StartAgendaRequestDTO {
    private String agendaId;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime durationAgenda;
}
