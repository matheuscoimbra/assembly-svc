package com.assembly.vote.assembly.domain.core.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AgendaRequestDTO implements Serializable {

    @NotNull(message = "informe o título da pauta")
    private String title;
    private String description;
}
