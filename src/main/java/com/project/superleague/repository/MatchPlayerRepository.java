package com.project.superleague.repository;

import com.project.superleague.model.MatchPlayer;
import com.project.superleague.service.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {
    Optional<MatchPlayer> findByMatchIdAndPlayerId(Long matchId, Long playerId);
    void deleteByMatchIdAndPlayerId(Long matchId, Long playerId) throws EntityNotFoundException;
}