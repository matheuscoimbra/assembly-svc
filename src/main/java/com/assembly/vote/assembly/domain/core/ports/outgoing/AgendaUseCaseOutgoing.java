package com.assembly.vote.assembly.domain.core.ports.outgoing;

import com.assembly.vote.assembly.domain.core.model.Agenda;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface AgendaUseCaseOutgoing {
    Mono<Agenda> findById(String agendaId);

    Flux<Agenda> findAllByFinishTimeBetween(String start, String end);

    Flux<Agenda> findAll(Pageable pageable);
}
