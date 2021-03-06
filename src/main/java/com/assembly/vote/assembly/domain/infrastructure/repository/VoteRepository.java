package com.assembly.vote.assembly.domain.infrastructure.repository;

import com.assembly.vote.assembly.domain.core.model.Vote;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VoteRepository extends ReactiveMongoRepository<Vote,String> {
}
