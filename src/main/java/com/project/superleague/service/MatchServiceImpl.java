package com.project.superleague.service;

import com.project.superleague.dto.MatchInsertDTO;
import com.project.superleague.dto.MatchUpdateDTO;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Match;
import com.project.superleague.model.Team;
import com.project.superleague.repository.MatchRepository;
import com.project.superleague.repository.TeamRepository;
import com.project.superleague.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchServiceImpl implements IMatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Transactional
    @Override
    public Match insertMatch(MatchInsertDTO dto) throws Exception {
        Match match = null;
        Team hostTeam = null;
        Team guestTeam = null;

        try {
            hostTeam = teamRepository.findById(dto.getHostTeamId()).get();
            guestTeam = teamRepository.findById(dto.getGuestTeamId()).get();
            match = matchRepository.save(Mapper.mapInsertDTOToMatch(dto, hostTeam, guestTeam));
            if (match.getId() == null) {
                throw new Exception("Insert error.");
            }
            log.info("Insert successful.");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return match;
    }

    @Transactional
    @Override
    public Match updateMatch(MatchUpdateDTO dto) throws EntityNotFoundException {
        Match updatedMatch;
        Match match;
        Team hostTeam = null;
        Team guestTeam = null;

        try {
            match = matchRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException(Match.class, dto.getId()));
            match.deleteHostTeam(match.getHostTeam());
            match.deleteGuestTeam(match.getGuestTeam());
            hostTeam = teamRepository.findById(dto.getHostTeamId()).get();
            guestTeam = teamRepository.findById(dto.getGuestTeamId()).get();
            updatedMatch = matchRepository.save(Mapper.mapUpdateDTOToMatch(dto, hostTeam, guestTeam));
            log.info("Update successful.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedMatch;
    }

    @Transactional
    @Override
    public Match deleteMatch(Long id) throws EntityNotFoundException {
        Match match = null;

        try {
            match = matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Match.class, id));
            matchRepository.deleteById(id);
            log.info("Deletion successful.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return match;
    }

    @Override
    public List<Match> getMatchByDate(Date date) throws EntityNotFoundException {
        List<Match> matches = new ArrayList<>();

        try {
            matches = matchRepository.findByMatchDate(date);
            if (matches.isEmpty()) {
                throw new EntityNotFoundException(Match.class, 0L);
            }
            log.info("Matches with date " + date + " were found.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return matches;
    }

    @Override
    public Match getMatchById(Long id) throws EntityNotFoundException {
        Match match;

        try {
            match = matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Match.class, id));
            log.info("Search by id " + id + " was successful");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return match;
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }
}