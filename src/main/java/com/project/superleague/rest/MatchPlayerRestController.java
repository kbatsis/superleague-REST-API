package com.project.superleague.rest;

import com.project.superleague.dto.*;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Match;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.service.IMatchPlayerService;
import com.project.superleague.service.exception.EntityAlreadyExistsException;
import com.project.superleague.service.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get player statistics for a given match by match id and player id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requested player statistics were found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchPlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Requested player statistics were not found.",
                    content = @Content)})
    @GetMapping("/matchesplayers/{matchId}/{playerId}")
    public ResponseEntity<Object> getMatchPlayerByMatchIdAndPlayerId(@PathVariable("matchId") Long matchId, @PathVariable("playerId") Long playerId) {
        MatchPlayer matchPlayer;

        try {
            matchPlayer = matchPlayerService.getMatchPlayerByMatchIdAndPlayerId(matchId, playerId);
            MatchPlayerReadOnlyDTO dto = Mapper.mapMatchPlayerToReadOnlyDTO(matchPlayer);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add player statistics for a given match.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player statistics for the given match added.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchPlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided or the requested player statistics already exist.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Player or match not found.",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content)})
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Operation(summary = "Update player statistics for a given match.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player statistics for the given match updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchPlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Player statistics for the given match were not found.",
                    content = @Content)})
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete player statistics for a given match.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player statistics for the given match were deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchPlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Player statistics for the given match were not found.",
                    content = @Content)})
    @DeleteMapping("/matchesplayers/{matchId}/{playerId}")
    public ResponseEntity<Object> deleteMatchPlayer(@PathVariable("matchId") Long matchId, @PathVariable("playerId") Long playerId) {
        try {
            MatchPlayer matchPlayer = matchPlayerService.deleteMatchPlayer(matchId, playerId);
            MatchPlayerReadOnlyDTO matchPlayerReadOnlyDTO = Mapper.mapMatchPlayerToReadOnlyDTO(matchPlayer);
            return new ResponseEntity<>(matchPlayerReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}