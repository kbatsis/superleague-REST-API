package com.project.superleague.service;

import com.project.superleague.dto.TeamInsertDTO;
import com.project.superleague.dto.TeamUpdateDTO;
import com.project.superleague.model.Team;
import com.project.superleague.service.exception.EntityNotFoundException;

import java.util.List;

public interface ITeamService {
    Team insertTeam(TeamInsertDTO dto) throws Exception;
    Team updateTeam(TeamUpdateDTO dto) throws EntityNotFoundException;
    Team deleteTeam(Long id) throws EntityNotFoundException;
    List<Team> getTeamByName(String teamname) throws EntityNotFoundException;
    Team getTeamById(Long id) throws EntityNotFoundException;
    List<Team> getAllTeams();
}