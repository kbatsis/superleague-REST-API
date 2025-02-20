package com.project.superleague.rest;

import com.project.superleague.dto.UserLoginDTO;
import com.project.superleague.service.IUserService;
import com.project.superleague.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRestController {
    private final IUserService userService;

    @Operation(summary = "Login with username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, returns a JSON Web Token.",
                    content = { @Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400", description = "Username and/or password is null.",
                    content = @Content)})
    @PostMapping("/login")
    public Object loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        String token = userService.verifyUser(userLoginDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}