package com.assembly.vote.assembly.domain.core.ports.incoming;

import com.assembly.vote.assembly.domain.core.model.Agenda;
import reactor.core.publisher.Mono;

public interface AgendaUseCaseIncoming {

    Mono<Agenda> save(Agenda agenda);
}
