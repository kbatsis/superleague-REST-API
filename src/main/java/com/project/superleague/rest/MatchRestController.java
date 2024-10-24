package com.project.superleague.rest;

import com.project.superleague.dto.*;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Match;
import com.project.superleague.service.IMatchService;
import com.project.superleague.service.exception.EntityNotFoundException;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchRestController {
    private final IMatchService matchService;

    @GetMapping("/matches")
    public ResponseEntity<Object> getMatchesByDate(@RequestParam("date") @DateTimeFormat(pattern = "ddMMyyyy") Date date) {
        List<Match> matches;

        try {
            matches = matchService.getMatchByDate(date);
            List<MatchReadOnlyDTO> matchesReadOnlyDTOS = new ArrayList<>();
            for (Match match : matches) {
                matchesReadOnlyDTOS.add(Mapper.mapMatchToReadOnlyDTO(match));
            }
            return new ResponseEntity<>(matchesReadOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<Object> getMatchById(@PathVariable("id") Long id) {
        Match match;

        try {
            match = matchService.getMatchById(id);
            MatchReadOnlyDTO dto = Mapper.mapMatchToReadOnlyDTO(match);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/matches/all")
    public ResponseEntity<List<MatchReadOnlyDTO>> getAllMatches() {
        List<Match> matches;

        matches = matchService.getAllMatches();
        List<MatchReadOnlyDTO> matchesReadOnlyDTOS = new ArrayList<>();
        for (Match match : matches) {
            matchesReadOnlyDTOS.add(Mapper.mapMatchToReadOnlyDTO(match));
        }
        return new ResponseEntity<>(matchesReadOnlyDTOS, HttpStatus.OK);
    }

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
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

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
            return new ResponseEntity<>("Not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Object> deleteMatch(@PathVariable("id") Long id) {
        try {
            Match match = matchService.deleteMatch(id);
            MatchReadOnlyDTO matchReadOnlyDTO = Mapper.mapMatchToReadOnlyDTO(match);
            return new ResponseEntity<>(matchReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Not found.", HttpStatus.BAD_REQUEST);
        }
    }
}