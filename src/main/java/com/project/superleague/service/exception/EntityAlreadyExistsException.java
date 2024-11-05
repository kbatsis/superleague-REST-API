package com.project.superleague.service.exception;

public class EntityAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 2L;

    public EntityAlreadyExistsException(Long matchId, Long playerId) {
        super("Entity with match id " + matchId + " and player id " + playerId + " already exists.");
    }
}