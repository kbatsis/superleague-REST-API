package com.project.superleague.rest;


import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerReadOnlyDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.dto.TeamReadOnlyDTO;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.IPlayerService;
import com.project.superleague.service.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlayerRestController {
    private final IPlayerService playerService;

    @Operation(summary = "Get players by their lastname. Given lastname can be missing ending letters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Players with the given lastname were found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid lastname given.",
                    content = @Content)})
    @GetMapping("/players")
    public ResponseEntity<Object> getPlayersByLastname(@RequestParam("lastname") String lastname) {
        List<Player> players;

        try {
            players = playerService.getPlayerByLastname(lastname);
            List<PlayerReadOnlyDTO> playersReadOnlyDTOS = new ArrayList<>();
            for (Player player : players) {
                playersReadOnlyDTOS.add(Mapper.mapPlayerToReadOnlyDTO(player));
            }
            return new ResponseEntity<>(playersReadOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get player by his id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player was found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Player was not found.",
                    content = @Content)})
    @GetMapping("/players/{id}")
    public ResponseEntity<Object> getPlayerById(@PathVariable("id") Long id) {
        Player player;

        try {
            player = playerService.getPlayerById(id);
            PlayerReadOnlyDTO dto = Mapper.mapPlayerToReadOnlyDTO(player);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player added.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Team not found.",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content)})
    @PostMapping("/players")
    public ResponseEntity<Object> addPlayer(@Valid @RequestBody PlayerInsertDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Player player = playerService.insertPlayer(dto);
            PlayerReadOnlyDTO playerReadOnlyDTO = Mapper.mapPlayerToReadOnlyDTO(player);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(playerReadOnlyDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(playerReadOnlyDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Operation(summary = "Update a player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Player or team not found.",
                    content = @Content)})
    @PutMapping("/players/{id}")
    public ResponseEntity<Object> updatePlayer(@PathVariable("id") Long id, @Valid @RequestBody PlayerUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Player player = playerService.updatePlayer(dto);
            PlayerReadOnlyDTO playerReadOnlyDTO = Mapper.mapPlayerToReadOnlyDTO(player);
            return new ResponseEntity<>(playerReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Player not found.",
                    content = @Content)})
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Object> deletePlayer(@PathVariable("id") Long id) {
        try {
            Player player = playerService.deletePlayer(id);
            PlayerReadOnlyDTO playerReadOnlyDTO = Mapper.mapPlayerToReadOnlyDTO(player);
            return new ResponseEntity<>(playerReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}