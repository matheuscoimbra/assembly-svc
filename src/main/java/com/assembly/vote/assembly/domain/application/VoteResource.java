package com.assembly.vote.assembly.domain.application;

import com.assembly.vote.assembly.domain.core.AssemblyFacade;
import com.assembly.vote.assembly.domain.core.model.Vote;
import com.assembly.vote.assembly.domain.core.model.dto.VoteRequestDTO;
import com.assembly.vote.assembly.domain.core.model.dto.VoteResponseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1/vote")
public class VoteResource {

    private final AssemblyFacade assemblyFacade;

    @ApiOperation(value = "API para votar uma pauta")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Retorno OK pauta votada."),
            @ApiResponse(code = 401, message = "Usuário UNABLE_TO_VOTE"),
            @ApiResponse(code = 422, message = "Pauta já fechou"),
            @ApiResponse(code = 429, message = "Usuário já votou na pauta"),
            @ApiResponse(code = 404, message = "Recurso não encontrado")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<VoteResponseDTO>> saveVote(@RequestBody @Valid VoteRequestDTO voteRequestDTO){
        return assemblyFacade.saveVote(voteRequestDTO).map((vote) -> new ResponseEntity<>(vote, HttpStatus.OK));
    }

}
