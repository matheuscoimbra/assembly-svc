package com.assembly.vote.assembly.domain.core.model.dto;

import com.assembly.vote.assembly.domain.core.model.Vote;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AgendaCompleteResponseDTO {
    private String id;
    private String title;
    private String description;
    private String startDatetime;
    private String FinishDateTime;
    private boolean open;
    private int totalVote;
    private int totalYes;
    private int totalNo;
    private List<Vote> votes = new ArrayList<>();

}
