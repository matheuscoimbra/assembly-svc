package com.assembly.vote.assembly.domain.infrastructure.adapter;

import com.assembly.vote.assembly.domain.infrastructure.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AssemblyVoteDatabaseAdapter {

    private final VoteRepository voteRepository;
}
