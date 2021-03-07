package com.assembly.vote.assembly.domain.core.model.dto;

import com.assembly.vote.assembly.domain.core.model.enums.VOTE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoteResponseDTO implements Serializable {
    private String cpf;
    private VOTE vote;

}
