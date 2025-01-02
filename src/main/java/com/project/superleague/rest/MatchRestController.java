package com.project.superleague.rest;

import com.project.superleague.dto.*;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Match;
import com.project.superleague.service.IMatchService;
import com.project.superleague.service.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchRestController {
    private final IMatchService matchService;

    @Operation(summary = "Get match by date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matches with the given date were found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Matches with the given date were not found.",
                    content = @Content)})
    @GetMapping("/matches")
    public ResponseEntity<Object> getMatchesByDate(@RequestParam("date") @DateTimeFormat(pattern = "ddMMyyyy") LocalDate date) {
        List<Match> matches;

        try {
            matches = matchService.getMatchByDate(date);
            List<MatchReadOnlyDTO> matchesReadOnlyDTOS = new ArrayList<>();
            for (Match match : matches) {
                matchesReadOnlyDTOS.add(Mapper.mapMatchToReadOnlyDTO(match));
            }
            return new ResponseEntity<>(matchesReadOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get match by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match was found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Match was not found.",
                    content = @Content)})
    @GetMapping("/matches/{id}")
    public ResponseEntity<Object> getMatchById(@PathVariable("id") Long id) {
        Match match;

        try {
            match = matchService.getMatchById(id);
            MatchReadOnlyDTO dto = Mapper.mapMatchToReadOnlyDTO(match);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a match.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Match added.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Host or guest team not found.",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content)})
    @PostMapping("/matches")
    public ResponseEntity<Object> addMatch(@Valid @RequestBody MatchInsertDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Match match = matchService.insertMatch(dto);
            MatchReadOnlyDTO matchReadOnlyDTO = Mapper.mapMatchToReadOnlyDTO(match);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(matchReadOnlyDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(matchReadOnlyDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Operation(summary = "Update a match.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Match or associated teams were not found.",
                    content = @Content)})
    @PutMapping("/matches/{id}")
    public ResponseEntity<Object> updateMatch(@PathVariable("id") Long id, @Valid @RequestBody MatchUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Match match = matchService.updateMatch(dto);
            MatchReadOnlyDTO matchReadOnlyDTO = Mapper.mapMatchToReadOnlyDTO(match);
            return new ResponseEntity<>(matchReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a match.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MatchReadOnlyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Match not found.",
                    content = @Content)})
    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Object> deleteMatch(@PathVariable("id") Long id) {
        try {
            Match match = matchService.deleteMatch(id);
            MatchReadOnlyDTO matchReadOnlyDTO = Mapper.mapMatchToReadOnlyDTO(match);
            return new ResponseEntity<>(matchReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}