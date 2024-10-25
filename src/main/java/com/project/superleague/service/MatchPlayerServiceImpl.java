package com.project.superleague.service;

import com.project.superleague.dto.MatchPlayerInsertDTO;
import com.project.superleague.dto.MatchPlayerUpdateDTO;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Match;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.model.Player;
import com.project.superleague.repository.MatchPlayerRepository;
import com.project.superleague.repository.MatchRepository;
import com.project.superleague.repository.PlayerRepository;
import com.project.superleague.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchPlayerServiceImpl implements IMatchPlayerService {
    private final MatchPlayerRepository matchPlayerRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    @Override
    public MatchPlayer insertMatchPlayer(MatchPlayerInsertDTO dto) throws Exception {
        MatchPlayer matchPlayer = null;
        Match match = null;
        Player player = null;

        try {
            match = matchRepository.findById(dto.getMatchId()).get();
            player = playerRepository.findById(dto.getPlayerId()).get();
            matchPlayer = matchPlayerRepository.save(Mapper.mapInsertDTOToMatchPlayer(dto, match, player));
            if (matchPlayer.getId() == null) {
                throw new Exception("Insert error.");
            }
            log.info("Inset successful.");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return matchPlayer;
    }

    @Transactional
    @Override
    public MatchPlayer updateMatchPlayer(MatchPlayerUpdateDTO dto) throws EntityNotFoundException {
        MatchPlayer updatedMatchPlayer;
        Match match = null;
        Player player = null;

        try {
            matchPlayerRepository.findByMatchIdAndPlayerId(dto.getMatchId(), dto.getPlayerId()).orElseThrow(() -> new EntityNotFoundException(MatchPlayer.class, 0L));
            match = matchRepository.findById(dto.getMatchId()).get();
            player = playerRepository.findById(dto.getPlayerId()).get();
            updatedMatchPlayer = matchPlayerRepository.save(Mapper.mapUpdateDTOToMatchPlayer(dto, match, player));
            log.info("Update successful.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedMatchPlayer;
    }

    @Override
    public MatchPlayer deleteMatchPlayer(Long matchId, Long playerId) throws EntityNotFoundException {
        return null;
    }

    @Override
    public MatchPlayer getMatchPlayerByMatchId(Long matchId) throws EntityNotFoundException {
        return null;
    }
}