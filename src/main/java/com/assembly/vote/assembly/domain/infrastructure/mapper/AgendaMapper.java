package com.assembly.vote.assembly.domain.infrastructure.mapper;

import com.assembly.vote.assembly.domain.core.model.Agenda;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaCompleteResponseDTO;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaRequestDTO;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface AgendaMapper {


    Agenda agendaFromRequest(AgendaRequestDTO agendaRequestDTO);

    AgendaResponseDTO fromAgenda(Agenda agenda);

    AgendaCompleteResponseDTO completeResponseFromAgenda(Agenda agenda);
}
