package com.project.superleague.mapper;

import com.project.superleague.dto.*;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import org.springframework.core.SpringVersion;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.HashSet;
import java.util.Set;

public class Mapper {
    private Mapper() {}

    public static Player mapInsertDTOToPlayer(PlayerInsertDTO dto, Team team) {
        Player player = new Player(null, dto.getFirstname(), dto.getLastname(), dto.getDateOfBirth(), dto.getNationality(), dto.getMonetaryValue(), dto.getPlayerRole()) ;
        player.addTeam(team);
        return player;
    }

    public static Player mapUpdateDTOToPlayer(PlayerUpdateDTO dto, Team team) {
        Player player = new Player(dto.getId(), dto.getFirstname(), dto.getLastname(), dto.getDateOfBirth(), dto.getNationality(), dto.getMonetaryValue(), dto.getPlayerRole());
        player.addTeam(team);
        return player;
    }

    public static PlayerReadOnlyDTO mapPlayerToReadOnlyDTO(Player player) {
        Long teamId = null;

        if (player.getTeam() != null) {
            teamId = player.getTeam().getId();
        }

        return new PlayerReadOnlyDTO(player.getId(), player.getDateOfBirth(), player.getFirstname(), player.getLastname(), player.getMonetaryValue(), player.getNationality(), player.getPlayerRole(), teamId);
    }

    public static Team mapInsertDTOtoTeam(TeamInsertDTO dto) {
        return new Team(null, dto.getTeamName(), dto.getFoundationYear(), dto.getCityName(), dto.getStadiumName(), dto.getCoachFirstname(), dto.getCoachLastname(), dto.getPresidentFirstname(),dto.getPresidentLastname());
    }

    public static Team mapUpdateDTOtoTeam(TeamUpdateDTO dto) {
        return new Team(dto.getId(), dto.getTeamName(), dto.getFoundationYear(), dto.getCityName(), dto.getStadiumName(), dto.getCoachFirstname(), dto.getCoachLastname(), dto.getPresidentFirstname(),dto.getPresidentLastname());
    }

    public static TeamReadOnlyDTO mapTeamToReadOnlyDTO(Team team) {
        return new TeamReadOnlyDTO(team.getId(), team.getTeamName(), team.getFoundationYear(), team.getCityName(), team.getStadiumName(), team.getCoachFirstname(), team.getCoachLastname(), team.getPresidentFirstname(), team.getPresidentLastname());
    }
}