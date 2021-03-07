package com.assembly.vote.assembly.domain.infrastructure.repository;

import com.assembly.vote.assembly.domain.core.model.Agenda;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AgendaRepository extends ReactiveMongoRepository<Agenda,String> {

    Flux<Agenda> findAllByIdNotNull(Pageable pageable);
}

