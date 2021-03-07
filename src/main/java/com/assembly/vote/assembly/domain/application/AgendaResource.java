package com.assembly.vote.assembly.domain.application;

import com.assembly.vote.assembly.domain.core.AssemblyFacade;
import com.assembly.vote.assembly.domain.core.model.Agenda;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaCompleteResponseDTO;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaRequestDTO;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaResponseDTO;
import com.assembly.vote.assembly.domain.core.model.dto.StartAgendaRequestDTO;
import io.swagger.annotations.*;
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

@Api(tags = "v1/agenda", value = "Grupo de API's para manipulação de pautas")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1/agenda")
public class AgendaResource {

    private final AssemblyFacade assemblyFacade;

    @ApiOperation(value = "API para criar uma pauta")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Retorno CREATED")})
    @PostMapping
    public Mono<ResponseEntity<AgendaResponseDTO>> saveAgenda(@RequestBody @Valid AgendaRequestDTO agendaRequestDTO){
        return assemblyFacade.saveAgenda(agendaRequestDTO).map((agenda) -> new ResponseEntity<>(agenda, HttpStatus.CREATED));
    }

    @ApiOperation(value = "API para iniciar uma pauta")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Retorno OK pauta iniciada."),
            @ApiResponse(code = 422, message = "Pauta já foi iniciada anteriormente"),
            @ApiResponse(code = 404, message = "Recurso não encontrado")})
    @PatchMapping("start")
    public Mono<Agenda>  startAgenda(StartAgendaRequestDTO startAgendaRequestDTO){
        return assemblyFacade.startAgenda(startAgendaRequestDTO);
    }

    @ApiOperation(value = "API para retornar pauta por Id")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "id", required = true)})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Retorno OK pauta."),
            @ApiResponse(code = 404, message = "Recurso não encontrado")})
    @GetMapping()
    public Mono<ResponseEntity<AgendaCompleteResponseDTO>> getOneById(@RequestParam("id") String id){
        return assemblyFacade.findAgendaById(id).map((agenda) -> new ResponseEntity<>(agenda, HttpStatus.OK));
    }

    @ApiOperation(value = "API para retornar pautas")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Retorno OK pauta.")})
    @GetMapping("all")
    public Flux<Agenda> findAllPage(@RequestParam("page") int pageIndex,
                                    @RequestParam("size") int pageSize){
        return assemblyFacade.findAll(PageRequest.of(pageIndex, pageSize));
    }


}
