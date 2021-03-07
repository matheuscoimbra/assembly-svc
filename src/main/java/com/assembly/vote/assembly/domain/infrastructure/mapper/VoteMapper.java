package com.assembly.vote.assembly.domain.infrastructure.mapper;

import com.assembly.vote.assembly.domain.core.model.Vote;
import com.assembly.vote.assembly.domain.core.model.dto.VoteRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface VoteMapper {


    Vote voteFromRequest(VoteRequestDTO voteRequestDTO);

}
