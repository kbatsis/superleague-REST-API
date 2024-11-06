package com.project.superleague.service;

import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.model.Player;
import com.project.superleague.service.exception.EntityNotFoundException;

import java.util.List;

public interface IPlayerService {
    Player insertPlayer(PlayerInsertDTO dto) throws EntityNotFoundException, Exception;
    Player updatePlayer(PlayerUpdateDTO dto) throws EntityNotFoundException;
    Player deletePlayer(Long id) throws EntityNotFoundException;
    List<Player> getPlayerByLastname(String lastname) throws EntityNotFoundException;
    Player getPlayerById(Long id) throws EntityNotFoundException;
}