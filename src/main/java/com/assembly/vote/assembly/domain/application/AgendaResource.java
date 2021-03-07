package com.assembly.vote.assembly.domain.application;

import com.assembly.vote.assembly.domain.core.AssemblyFacade;
import com.assembly.vote.assembly.domain.core.model.Agenda;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaCompleteResponseDTO;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaRequestDTO;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaResponseDTO;
import com.assembly.vote.assembly.domain.core.model.dto.StartAgendaRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1/agenda")
public class AgendaResource {

    private final AssemblyFacade assemblyFacade;

    @PostMapping
    public Mono<ResponseEntity<AgendaResponseDTO>> saveAgenda(@RequestBody @Valid AgendaRequestDTO agendaRequestDTO){
        return assemblyFacade.saveAgenda(agendaRequestDTO).map((agenda) -> new ResponseEntity<>(agenda, HttpStatus.OK));
    }

    @PatchMapping("start")
    public void startAgenda(StartAgendaRequestDTO startAgendaRequestDTO){
        assemblyFacade.startAgenda(startAgendaRequestDTO);
    }


    @GetMapping()
    public Mono<ResponseEntity<AgendaCompleteResponseDTO>> getOneById(@RequestParam("id") String id){
        return assemblyFacade.findAgendaById(id).map((agenda) -> new ResponseEntity<>(agenda, HttpStatus.OK));
    }

    @GetMapping("all")
    public Flux<Agenda> findAllPage(@RequestParam("page") int pageIndex,
                                    @RequestParam("size") int pageSize){
        return assemblyFacade.findAll(PageRequest.of(pageIndex, pageSize));
    }


}
