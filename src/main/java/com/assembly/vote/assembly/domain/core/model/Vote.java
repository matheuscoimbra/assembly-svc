package com.assembly.vote.assembly.domain.core.model;

import com.assembly.vote.assembly.domain.core.model.enums.VOTE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Builder
@Data
@Document(collection = "vote")
public class Vote {
    @Id
    private String id;
    private String cpf;
    private VOTE vote;
    private String agendaId;
}
