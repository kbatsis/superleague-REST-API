package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserLoginDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;
}