package com.project.superleague.service;

import com.project.superleague.dto.TeamInsertDTO;
import com.project.superleague.dto.TeamUpdateDTO;
import com.project.superleague.model.Team;
import com.project.superleague.repository.TeamRepository;
import com.project.superleague.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamServiceImpl implements ITeamService {
    private final TeamRepository teamRepository;

    @Override
    public Team insertTeam(TeamInsertDTO dto) throws Exception {
        return null;
    }

    @Override
    public Team updateTeam(TeamUpdateDTO dto) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Team deleteTeam(Long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public List<Team> getTeamByName(String teamname) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Team getTeamById(Long id) throws EntityNotFoundException {
        return null;
    }
}