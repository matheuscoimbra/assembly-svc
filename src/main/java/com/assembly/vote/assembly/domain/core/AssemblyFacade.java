package com.assembly.vote.assembly.domain.core;

import com.assembly.vote.assembly.domain.core.model.Agenda;
import com.assembly.vote.assembly.domain.core.model.Vote;
import com.assembly.vote.assembly.domain.core.model.dto.*;
import com.assembly.vote.assembly.domain.core.model.enums.AVAILABLEUSER;
import com.assembly.vote.assembly.domain.core.model.enums.VOTE;
import com.assembly.vote.assembly.domain.core.ports.incoming.AgendaRequestIncoming;
import com.assembly.vote.assembly.domain.core.ports.incoming.VoteRequestIncoming;
import com.assembly.vote.assembly.domain.infrastructure.adapter.AssemblyAgendaDatabaseAdapter;
import com.assembly.vote.assembly.domain.infrastructure.adapter.AssemblyVoteDatabaseAdapter;
import com.assembly.vote.assembly.domain.infrastructure.mapper.AgendaMapper;
import com.assembly.vote.assembly.domain.infrastructure.mapper.VoteMapper;
import com.assembly.vote.assembly.infrastructure.exception.DomainBusinessException;
import com.assembly.vote.assembly.infrastructure.exception.NotFoundException;
import com.assembly.vote.assembly.infrastructure.exception.TooManyRequestException;
import com.assembly.vote.assembly.infrastructure.exception.UnauthorizedException;
import com.assembly.vote.assembly.infrastructure.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AssemblyFacade implements AgendaRequestIncoming, VoteRequestIncoming {

    private final AssemblyAgendaDatabaseAdapter agendaDatabaseAdapter;

    private final AssemblyVoteDatabaseAdapter voteDatabaseAdapter;

    private final AgendaMapper agendaMapper;

    private final VoteMapper voteMapper;
    private final WebClient webClient;

    @Transactional
    @Override
    public Mono<AgendaResponseDTO> saveAgenda(AgendaRequestDTO agendaRequestDTO) {
        var agendaToSave = agendaMapper.agendaFromRequest(agendaRequestDTO);
        return agendaDatabaseAdapter.save(agendaToSave).flatMap(agenda -> Mono.just(agendaMapper.fromAgenda(agenda)));
    }

    @Override
    public Mono<AgendaCompleteResponseDTO> findAgendaById(String id) {
        return agendaDatabaseAdapter.findById(id).flatMap(agenda -> Mono.just(agendaMapper.completeResponseFromAgenda(agenda))
                .zipWith(voteDatabaseAdapter.findAllByAgenda(agenda.getId())
                        .collectList(),
                (u, p) -> {
                    u.setVotes(p);
                    return u;
                })
        );
    }

    @Override
    public Mono<Agenda> startAgenda(StartAgendaRequestDTO startAgendaRequestDTO) {
       return agendaDatabaseAdapter.findById(startAgendaRequestDTO.getAgendaId()).flatMap(
                agenda -> {
                    if(agenda.isOpen()){
                      throw  new DomainBusinessException("Pauta " + agenda.getTitle() + " já foi iniciada anteriormente");
                    }
                    if(!agenda.isOpen() && !StringUtils.isEmpty(agenda.getFinishDateTime())){
                        throw  new DomainBusinessException("Pauta " + agenda.getTitle() + " já foi fechada");
                    }
                    log.info("Iniciando pauta " + agenda.getTitle());
                    var now = LocalDateTime.now();
                    agenda.setStartDatetime(DateUtils.toString(DateUtils.convertToDateViaInstant(now)));
                    if (startAgendaRequestDTO.getDurationAgenda() == null) {
                        agenda.setFinishDateTime(DateUtils.toString(DateUtils.convertToDateViaInstant(now.plusMinutes(1))));
                    } else {
                        var finishAgendaTime = now.plusHours(startAgendaRequestDTO.getDurationAgenda().getHour()).plusMinutes(startAgendaRequestDTO.getDurationAgenda().getMinute());
                        agenda.setFinishDateTime(DateUtils.toString(DateUtils.convertToDateViaInstant(finishAgendaTime)));
                    }
                    agenda.setOpen(true);
                    return agendaDatabaseAdapter.save(agenda);
                }
        ).switchIfEmpty(Mono.error(new NotFoundException("Pauta não encontrado para id " + startAgendaRequestDTO.getAgendaId())));
    }

    @Override
    public Flux<Agenda> findAll(Pageable pageable) {
        return agendaDatabaseAdapter.findAll(pageable);
    }



    @Transactional
    @Override
    public Mono<VoteResponseDTO> saveVote(VoteRequestDTO voteRequestDTO) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/{CPF}").build(voteRequestDTO.getCpf())).retrieve().onStatus(HttpStatus.NOT_FOUND::equals,
                clientResponse -> clientResponse.bodyToMono(NotFoundException.class)
                        .flatMap(Mono::error)).bodyToMono(ValidCPFResult.class)
                .flatMap(resultRequest -> {
                    if (resultRequest.getStatus().equals(AVAILABLEUSER.UNABLE_TO_VOTE.toString())) {
                        return Mono.error(new UnauthorizedException("Usuário não pode executar opração"));
                    } else {
                        return getVoteResponse(voteRequestDTO);
                    }
                });
    }

    private Mono<VoteResponseDTO> getVoteResponse(VoteRequestDTO voteRequestDTO) {
        return agendaDatabaseAdapter.findById(voteRequestDTO.getAgendaId()).switchIfEmpty(Mono.error(new NotFoundException("Pauta não encontrado para id " + voteRequestDTO.getAgendaId())))
                .flatMap(agenda1 -> {
                    if (agenda1.isOpen() && DateUtils.toLocalDtFromString(agenda1.getFinishDateTime()).isBefore(LocalDateTime.now())) {
                       throw new DomainBusinessException("Pauta " + agenda1.getTitle() + " já fechou");
                    } else if (agenda1.isOpen()) {
                        var voteToSave = voteMapper.voteFromRequest(voteRequestDTO);
                        return isUserAlreadyVotedAgenda(voteRequestDTO, agenda1, voteToSave);
                    }
                    return Mono.empty();
                }).switchIfEmpty( Mono.error(new DomainBusinessException("Pauta fechada"))).flatMap(vote -> {
                            if (vote == null) {
                                Mono.error(new DomainBusinessException("Pauta fechada"));
                            }
                            return Mono.just(getVoteResponseDTO(vote));
                        }
                );
    }

    private Mono<Vote> isUserAlreadyVotedAgenda(VoteRequestDTO voteRequestDTO, Agenda agenda, Vote voteToSave) {
        return voteDatabaseAdapter.findByCpfAndAgenda(voteToSave.getCpf(), voteRequestDTO.getAgendaId()).count().flatMap(totalVotes -> {
            if (totalVotes.intValue() == 0) {
                var uservote = voteDatabaseAdapter.save(voteToSave);
                if (voteToSave.getVote().equals(VOTE.SIM)) {
                    agenda.incrementTotalYes();
                } else {
                    agenda.incrementTotalNo();
                }
                agendaDatabaseAdapter.save(agenda).subscribe();
                return uservote;
            }
            throw new TooManyRequestException("Usuário ja votou na pauta " + agenda.getTitle());
        });
    }

    private VoteResponseDTO getVoteResponseDTO(Vote vote) {
        return VoteResponseDTO.builder().vote(vote.getVote()).cpf(vote.getCpf()).build();
    }
}
