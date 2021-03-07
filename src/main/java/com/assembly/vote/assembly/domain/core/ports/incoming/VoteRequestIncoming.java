package com.assembly.vote.assembly.domain.core.ports.incoming;

import com.assembly.vote.assembly.domain.core.model.dto.VoteRequestDTO;
import com.assembly.vote.assembly.domain.core.model.dto.VoteResponseDTO;
import reactor.core.publisher.Mono;

public interface VoteRequestIncoming {

    Mono<VoteResponseDTO> saveVote(VoteRequestDTO voteRequestDTO);
}
