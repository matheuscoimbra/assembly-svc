package com.assembly.vote.assembly.domain.core.ports.incoming;

import com.assembly.vote.assembly.domain.core.model.Vote;
import reactor.core.publisher.Mono;

public interface VoteUseCaseIncoming {

    Mono<Vote> save(Vote vote);


}
