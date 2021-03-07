package com.assembly.vote.assembly.domain.core.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserNotificationDTO {
    private List<String> cpf;
    private AgendaResultDTO notification;
}