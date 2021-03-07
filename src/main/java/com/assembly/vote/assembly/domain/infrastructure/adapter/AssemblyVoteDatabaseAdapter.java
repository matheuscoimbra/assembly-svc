package com.assembly.vote.assembly.domain.infrastructure.adapter;

import com.assembly.vote.assembly.domain.core.model.Vote;
import com.assembly.vote.assembly.domain.core.ports.incoming.VoteUseCaseIncoming;
import com.assembly.vote.assembly.domain.core.ports.outgoing.VoteUseCaseOutgoing;
import com.assembly.vote.assembly.domain.infrastructure.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
@Component
public class AssemblyVoteDatabaseAdapter implements VoteUseCaseIncoming, VoteUseCaseOutgoing {

    private final VoteRepository voteRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Vote> save(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public Flux<Vote> findByCpfAndAgenda(String cpf, String agenda) {
        Query query = new Query(Criteria.where("cpf").is(cpf).and("agendaId").is(agenda));
        return mongoTemplate.find(query,Vote.class);
    }

    @Override
    public Flux<Vote> getAll() {
        return voteRepository.findAll();
    }

    @Override
    public Flux<Vote> findAllByAgenda(String id) {
        Query query = new Query(Criteria.where("agendaId").is(id));
        return mongoTemplate.find(query,Vote.class);
    }
}
