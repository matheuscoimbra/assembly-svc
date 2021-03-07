package com.assembly.vote.assembly.domain.infrastructure.repository;

import com.assembly.vote.assembly.domain.core.model.Vote;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoteRepository extends ReactiveMongoRepository<Vote,String> {

    Flux<Vote> findAllByCpf(Mono<String> cpf);
}
