package com.project.superleague.service;

import com.project.superleague.dto.MatchInsertDTO;
import com.project.superleague.dto.MatchUpdateDTO;
import com.project.superleague.model.Match;
import com.project.superleague.service.exception.EntityNotFoundException;

import java.util.Date;
import java.util.List;

public interface IMatchService {
    Match insertMatch(MatchInsertDTO dto) throws Exception;
    Match updateMatch(MatchUpdateDTO dto) throws EntityNotFoundException;
    Match deleteMatch(Long id) throws EntityNotFoundException;
    List<Match> getMatchByDate(Date date) throws EntityNotFoundException;
    Match getMatchById(Long id) throws EntityNotFoundException;
}
