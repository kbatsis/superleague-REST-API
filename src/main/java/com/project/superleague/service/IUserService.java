package com.project.superleague.service;

import com.project.superleague.dto.UserLoginDTO;

public interface IUserService {
    String verifyUser(UserLoginDTO userLoginDTO);
}
