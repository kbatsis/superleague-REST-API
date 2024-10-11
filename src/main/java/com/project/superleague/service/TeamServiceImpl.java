package com.project.superleague.service;

import com.project.superleague.dto.TeamInsertDTO;
import com.project.superleague.dto.TeamUpdateDTO;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Team;
import com.project.superleague.repository.TeamRepository;
import com.project.superleague.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamServiceImpl implements ITeamService {
    private final TeamRepository teamRepository;

    @Transactional
    @Override
    public Team insertTeam(TeamInsertDTO dto) throws Exception {
        Team team = null;

        try {
            team = teamRepository.save(Mapper.mapInsertDTOtoTeam(dto));
            if (team.getId() == null) {
                throw new Exception("Insert error.");
            }
            log.info("Insert successful.");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return team;
    }

    @Transactional
    @Override
    public Team updateTeam(TeamUpdateDTO dto) throws EntityNotFoundException {
        Team updatedTeam;

        try {
            teamRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException(Team.class, dto.getId()));
            updatedTeam = teamRepository.save(Mapper.mapUpdateDTOtoTeam(dto));
            log.info("Update successful.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedTeam;
    }

    @Transactional
    @Override
    public Team deleteTeam(Long id) throws EntityNotFoundException {
        Team team = null;

        try {
            team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Team.class, id));
            teamRepository.deleteById(id);
            log.info("Deletion successful.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return team;
    }

    @Override
    public List<Team> getTeamByName(String teamname) throws EntityNotFoundException {
        List<Team> teams = new ArrayList<>();

        try {
            teams = teamRepository.findByTeamNameStartingWith(teamname);
            if (teams.isEmpty()) {
                throw new EntityNotFoundException(Team.class, 0L);
            }
            log.info("Teams starting with " + teamname + " were found.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return teams;
    }

    @Override
    public Team getTeamById(Long id) throws EntityNotFoundException {
        Team team;

        try {
            team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Team.class, id));
            log.info("Search by id " + id + " was successful");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return team;
    }
}