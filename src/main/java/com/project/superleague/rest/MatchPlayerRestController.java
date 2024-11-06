package com.project.superleague.rest;

import com.project.superleague.dto.*;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Match;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.service.IMatchPlayerService;
import com.project.superleague.service.exception.EntityAlreadyExistsException;
import com.project.superleague.service.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchPlayerRestController {
    private final IMatchPlayerService matchPlayerService;

    @GetMapping("/matchesplayers/{matchId}/{playerId}")
    public ResponseEntity<Object> getMatchPlayerByMatchIdAndPlayerId(@PathVariable("matchId") Long matchId, @PathVariable("playerId") Long playerId) {
        MatchPlayer matchPlayer;

        try {
            matchPlayer = matchPlayerService.getMatchPlayerByMatchIdAndPlayerId(matchId, playerId);
            MatchPlayerReadOnlyDTO dto = Mapper.mapMatchPlayerToReadOnlyDTO(matchPlayer);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/matchesplayers")
    public ResponseEntity<Object> addMatchPlayer(@Valid @RequestBody MatchPlayerInsertDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            MatchPlayer matchPlayer = matchPlayerService.insertMatchPlayer(dto);
            MatchPlayerReadOnlyDTO matchPlayerReadOnlyDTO = Mapper.mapMatchPlayerToReadOnlyDTO(matchPlayer);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{matchId}/{playerId}")
                    .buildAndExpand(matchPlayerReadOnlyDTO.getMatchId(), matchPlayerReadOnlyDTO.getPlayerId())
                    .toUri();
            return ResponseEntity.created(location).body(matchPlayerReadOnlyDTO);
        } catch (EntityAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping("/matchesplayers/{matchId}/{playerId}")
    public ResponseEntity<Object> updateMatchPlayer(@PathVariable("matchId") Long matchId, @PathVariable("playerId") Long playerId, @Valid @RequestBody MatchPlayerUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(matchId, dto.getMatchId()) || !Objects.equals(playerId, dto.getPlayerId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            MatchPlayer matchPlayer = matchPlayerService.updateMatchPlayer(dto);
            MatchPlayerReadOnlyDTO matchPlayerReadOnlyDTO = Mapper.mapMatchPlayerToReadOnlyDTO(matchPlayer);
            return new ResponseEntity<>(matchPlayerReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/matchesplayers/{matchId}/{playerId}")
    public ResponseEntity<Object> deleteMatchPlayer(@PathVariable("matchId") Long matchId, @PathVariable("playerId") Long playerId) {
        try {
            MatchPlayer matchPlayer = matchPlayerService.deleteMatchPlayer(matchId, playerId);
            MatchPlayerReadOnlyDTO matchPlayerReadOnlyDTO = Mapper.mapMatchPlayerToReadOnlyDTO(matchPlayer);
            return new ResponseEntity<>(matchPlayerReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}