package com.assembly.vote.assembly.domain.infrastructure.repository;

import com.assembly.vote.assembly.domain.core.model.Agenda;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AgendaRepository extends ReactiveMongoRepository<Agenda,String> {
}

