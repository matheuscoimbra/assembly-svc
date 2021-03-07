package com.assembly.vote.assembly.domain.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "agenda")
public class Agenda implements Serializable {

    @Id
    private String id;
    private String title;
    private String description;
    private String startDatetime;
    private String finishDateTime;
    private boolean open;
    private int totalYes;
    private int totalNo;

    public void incrementTotalYes(){
        this.totalYes++;
    }
    public void incrementTotalNo(){
        this.totalNo++;
    }

    public int getTotalVote(){
        return this.totalNo+this.totalYes;
    }

}
