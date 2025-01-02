package com.project.superleague.rest;

import com.project.superleague.dto.*;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.ITeamService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamRestController {
    private final ITeamService teamService;

    @Operation(summary = "Get teams by their name. Given team name can be missing ending letters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teams with the given team name were found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid team name given.",
                    content = @Content)})
    @GetMapping("/teams")
    public ResponseEntity<Object> getTeamsByTeamName(@RequestParam("teamname") String teamname) {
        List<Team> teams;

        try {
            teams = teamService.getTeamByName(teamname);
            List<TeamReadOnlyDTO> teamsReadOnlyDTOS = new ArrayList<>();
            for (Team team : teams) {
                teamsReadOnlyDTOS.add(Mapper.mapTeamToReadOnlyDTO(team));
            }
            return new ResponseEntity<>(teamsReadOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get team by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team was found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Team was not found.",
                    content = @Content)})
    @GetMapping("/teams/{id}")
    public ResponseEntity<Object> getTeamById(@PathVariable("id") Long id) {
        Team team;

        try {
            team = teamService.getTeamById(id);
            TeamReadOnlyDTO dto = Mapper.mapTeamToReadOnlyDTO(team);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Team added.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided.",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content)})
    @PostMapping("/teams")
    public ResponseEntity<Object> addTeam(@Valid @RequestBody TeamInsertDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Team team = teamService.insertTeam(dto);
            TeamReadOnlyDTO teamReadOnlyDTO = Mapper.mapTeamToReadOnlyDTO(team);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(teamReadOnlyDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(teamReadOnlyDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Operation(summary = "Update a team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Team not found.",
                    content = @Content)})
    @PutMapping("/teams/{id}")
    public ResponseEntity<Object> updateTeam(@PathVariable("id") Long id, @Valid @RequestBody TeamUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Team team = teamService.updateTeam(dto);
            TeamReadOnlyDTO teamReadOnlyDTO = Mapper.mapTeamToReadOnlyDTO(team);
            return new ResponseEntity<>(teamReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Team not found.",
                    content = @Content)})
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Object> deleteTeam(@PathVariable("id") Long id) {
        try {
            Team team = teamService.deleteTeam(id);
            TeamReadOnlyDTO teamReadOnlyDTO = Mapper.mapTeamToReadOnlyDTO(team);
            return new ResponseEntity<>(teamReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}