package com.project.superleague.service;

import com.project.superleague.dto.MatchPlayerInsertDTO;
import com.project.superleague.dto.MatchPlayerUpdateDTO;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.service.exception.EntityAlreadyExistsException;
import com.project.superleague.service.exception.EntityNotFoundException;

public interface IMatchPlayerService {
    MatchPlayer insertMatchPlayer(MatchPlayerInsertDTO dto) throws EntityAlreadyExistsException, Exception;
    MatchPlayer updateMatchPlayer(MatchPlayerUpdateDTO dto) throws EntityNotFoundException;
    MatchPlayer deleteMatchPlayer(Long matchId, Long playerId) throws EntityNotFoundException;
    MatchPlayer getMatchPlayerByMatchIdAndPlayerId(Long matchId, Long playerId) throws EntityNotFoundException;
}