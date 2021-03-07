package com.assembly.vote.assembly.domain.core.model.dto;

import com.assembly.vote.assembly.domain.core.model.enums.VOTE;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VoteRequestDTO {
    @NotNull(message = "Informe um CPF válido")
    private String cpf;
    @NotNull(message = "Informe a pauta à votar")
    private String agendaId;
    @NotNull(message = "Informe (SIM/NÃO) a pauta à votar")
    private VOTE vote;

}
