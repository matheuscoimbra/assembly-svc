package com.assembly.vote.assembly.domain.infrastructure.schedule;

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

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
@Component
public class VerifyAgendaClosed {

    private final AssemblyAgendaDatabaseAdapter agendaDatabaseAdapter;
    private final AssemblyVoteDatabaseAdapter voteDatabaseAdapter;

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    @Async
    public void sendTask() {
        log.info("[T1] Buscando pautas");

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
            });
        }).subscribe();



    }
}
