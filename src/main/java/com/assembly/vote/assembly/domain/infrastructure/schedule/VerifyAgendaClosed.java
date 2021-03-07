package com.assembly.vote.assembly.domain.infrastructure.schedule;

import com.assembly.vote.assembly.domain.application.publisher.NotificationSender;
import com.assembly.vote.assembly.domain.core.model.Vote;
import com.assembly.vote.assembly.domain.core.model.dto.AgendaResultDTO;
import com.assembly.vote.assembly.domain.core.model.dto.UserNotificationDTO;
import com.assembly.vote.assembly.domain.infrastructure.adapter.AssemblyAgendaDatabaseAdapter;
import com.assembly.vote.assembly.domain.infrastructure.adapter.AssemblyVoteDatabaseAdapter;
import com.assembly.vote.assembly.infrastructure.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
@Component
public class VerifyAgendaClosed {

    private final AssemblyAgendaDatabaseAdapter agendaDatabaseAdapter;
    private final AssemblyVoteDatabaseAdapter voteDatabaseAdapter;
    private final NotificationSender notificationSender;

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    @Async
    public void sendTask() {

        var dateSearchBegin = LocalDateTime.now().withSecond(0);
        var dateSearchBeginEnd = dateSearchBegin.plusSeconds(59);

        var start = DateUtils.toString(DateUtils.convertToDateViaInstant(dateSearchBegin));
        var end =  DateUtils.toString(DateUtils.convertToDateViaInstant(dateSearchBeginEnd));
        log.info("[T2] Buscando pautas de " + start + " até " + end);
        agendaDatabaseAdapter.findAllByFinishTimeBetween(start,end).collectList().doOnNext(agenda -> {
            agenda.forEach(agenda1 -> {
                log.info("Fechando pauta "+agenda1.getTitle());
                agenda1.setOpen(false);
                agendaDatabaseAdapter.save(agenda1).subscribe();
                //TODO:notifica
                voteDatabaseAdapter.findAllByAgenda(agenda1.getId()).collectList().doOnNext(votes -> {
                    if(votes!=null && !votes.isEmpty()) {
                        var CPFs = votes.stream().map(Vote::getCpf).collect(Collectors.toList());
                        AgendaResultDTO agendaResultDTO = AgendaResultDTO.builder()
                                .totalNo(agenda1.getTotalNo()).totalYes(agenda1.getTotalYes())
                                .totalVotes(agenda1.getTotalNo() + agenda1.getTotalYes()).build();
                        UserNotificationDTO userNotificationDTO = UserNotificationDTO.builder().cpf(CPFs).notification(agendaResultDTO).build();
                        notificationSender.send(userNotificationDTO);
                    }
                }).subscribe();
            });
        }).subscribe();



    }
}
