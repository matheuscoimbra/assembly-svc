package com.assembly.vote.assembly.domain.core.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgendaResultDTO {
    private int totalYes;
    private int totalNo;
    private int totalVotes;
}
