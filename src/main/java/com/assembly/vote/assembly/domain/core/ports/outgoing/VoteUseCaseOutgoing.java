package com.assembly.vote.assembly.domain.core.ports.outgoing;

import com.assembly.vote.assembly.domain.core.model.Vote;
import reactor.core.publisher.Flux;

public interface VoteUseCaseOutgoing {
    Flux<Vote> findByCpfAndAgenda(String cpf, String agenda);

    Flux<Vote> getAll();

    Flux<Vote> findAllByAgenda(String id);
}
