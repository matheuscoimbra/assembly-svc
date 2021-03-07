package com.assembly.vote.assembly.domain.infrastructure.adapter;

import com.assembly.vote.assembly.domain.core.model.Agenda;
import com.assembly.vote.assembly.domain.core.model.Vote;
import com.assembly.vote.assembly.domain.core.ports.incoming.AgendaUseCaseIncoming;
import com.assembly.vote.assembly.domain.core.ports.outgoing.AgendaUseCaseOutgoing;
import com.assembly.vote.assembly.domain.infrastructure.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Component
public class AssemblyAgendaDatabaseAdapter implements AgendaUseCaseIncoming, AgendaUseCaseOutgoing {

    private final AgendaRepository agendaRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Agenda> save(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    @Override
    public Mono<Agenda> findById(String agendaId) {
        return agendaRepository.findById(agendaId);
    }

    @Override
    public Flux<Agenda> findAllByFinishTimeBetween(String start, String end) {
        Query query = new Query(Criteria.where("finishDateTime").gte(start).lte(end).and("open").is(true));
        return mongoTemplate.find(query, Agenda.class);    }

    @Override
    public Flux<Agenda> findAll(Pageable pageable) {
        return agendaRepository.findAllByIdNotNull(pageable);
    }
}
