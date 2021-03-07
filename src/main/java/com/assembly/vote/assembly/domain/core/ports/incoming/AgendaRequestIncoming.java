package com.assembly.vote.assembly.domain.core.ports.incoming;

import com.assembly.vote.assembly.domain.core.model.Agenda;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaCompleteResponseDTO;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaRequestDTO;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaResponseDTO;
import com.assembly.vote.assembly.domain.core.model.dto.StartAgendaRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AgendaRequestIncoming {

    Mono<AgendaResponseDTO> saveAgenda(AgendaRequestDTO agendaRequestDTO);

    Mono<AgendaCompleteResponseDTO> findAgendaById(String id);

    void startAgenda(StartAgendaRequestDTO startAgendaRequestDTO);

    Flux<Agenda> findAll(Pageable pageable);
}
