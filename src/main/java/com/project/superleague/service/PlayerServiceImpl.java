package com.project.superleague.service;

import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.repository.PlayerRepository;
import com.project.superleague.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerServiceImpl implements IPlayerService {
    private final PlayerRepository playerRepository;

    @Transactional
    @Override
    public Player insertPlayer(PlayerInsertDTO dto) throws Exception {
        Player player = null;
        Team team = null;

        try {
            //if (dto.getTeamName() != null) {
            //    team = teamRepository.findByTeamName(dto.getTeamName()).orElse(null);
            //}
            player = playerRepository.save(Mapper.mapInsertDTOToPlayer(dto, team));
            if (player.getId() == null) {
                throw new Exception("Insert error.");
            }
            log.info("Insert successful.");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return player;
    }

    @Transactional
    @Override
    public Player updatePlayer(PlayerUpdateDTO dto) throws EntityNotFoundException {
        Player player;
        Player updatedPlayer;
        Team team = null;
        Set<MatchPlayer> matchPlayer;

        try {
            //if (dto.getTeamName() != null) {
            //    team = teamRepository.findByTeamName(dto.getTeamName()).orElse(null);
            //}
            player = playerRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException(Player.class, dto.getId()));
            matchPlayer = new HashSet<>(player.getAllMatchesPlayers());
            updatedPlayer = playerRepository.save(Mapper.mapUpdateDTOToPlayer(dto, team, matchPlayer));
            log.info("Update successful.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedPlayer;
    }

    @Transactional
    @Override
    public Player deletePlayer(Long id) throws EntityNotFoundException {
        Player player = null;

        try {
            player = playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Player.class, id));
            playerRepository.deleteById(id);
            log.info("Deletion successful.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return player;
    }

    @Override
    public List<Player> getPlayerByLastname(String lastname) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Player getPlayerById(Long id) throws EntityNotFoundException {
        return null;
    }
}