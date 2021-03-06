package com.assembly.vote.assembly.domain.infrastructure.adapter;

import com.assembly.vote.assembly.domain.infrastructure.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AssemblyAgendaDatabaseAdapter {

    private final AgendaRepository agendaRepository;
}
