package com.project.superleague.mapper;

import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerReadOnlyDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
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
        return new Player(null, dto.getFirstname(), dto.getLastname(), dto.getDateOfBirth(), dto.getNationality(), dto.getMonetaryValue(), dto.getPlayerRole(), team);
    }

    public static Player mapUpdateDTOToPlayer(PlayerUpdateDTO dto, Team team) {
        return new Player(dto.getId(), dto.getFirstname(), dto.getLastname(), dto.getDateOfBirth(), dto.getNationality(), dto.getMonetaryValue(), dto.getPlayerRole(), team);
    }

    public static PlayerReadOnlyDTO mapPlayerToReadOnlyDTO(Player player) {
        String teamName = null;
        if (player.getTeam() != null) {
            teamName = player.getTeam().getTeamName();
        }

        return new PlayerReadOnlyDTO(player.getId(), player.getDateOfBirth(), player.getFirstname(), player.getLastname(), player.getMonetaryValue(), player.getNationality(), player.getPlayerRole(), teamName);
    }
}