package com.assembly.vote.assembly.domain.application;

import com.assembly.vote.assembly.domain.core.AssemblyFacade;
import com.assembly.vote.assembly.domain.core.model.Vote;
import com.assembly.vote.assembly.domain.core.model.dto.VoteRequestDTO;
import com.assembly.vote.assembly.domain.core.model.dto.VoteResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1/vote")
public class VoteResource {

    private final AssemblyFacade assemblyFacade;

    @PostMapping
    public Mono<ResponseEntity<VoteResponseDTO>> saveVote(@RequestBody @Valid VoteRequestDTO voteRequestDTO){
        return assemblyFacade.saveVote(voteRequestDTO).map((vote) -> new ResponseEntity<>(vote, HttpStatus.OK));
    }

}
