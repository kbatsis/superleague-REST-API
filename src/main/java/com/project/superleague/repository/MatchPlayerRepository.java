package com.project.superleague.repository;

import com.project.superleague.model.MatchPlayer;
import com.project.superleague.model.MatchPlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, MatchPlayerId> {
    Optional<MatchPlayer> findByMatchIdAndPlayerId(Long matchId, Long playerId);
    void deleteByMatchIdAndPlayerId(Long matchId, Long playerId);
}