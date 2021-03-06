package com.assembly.vote.assembly.domain.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@Document(collection = "agenda")
public class Agenda implements Serializable {

    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime startDatetime;
    private LocalDateTime FinishDateTime;
    private int totalVotes;
    private int totalYes;
    private int totalNo;
    @JsonIgnore
    @DBRef
    private List<Vote> votes;

}
