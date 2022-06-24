package com.federicoioan.alternativeschool.service.IService;

import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.JwtDto;
import com.federicoioan.alternativeschool.model.dto.UserLoginDto;
import com.federicoioan.alternativeschool.model.dto.UserRegistrationDto;


public interface AuthService {

    JwtDto authenticateUser(UserLoginDto loginRequest);

    User registerUser(UserRegistrationDto signUpRequest);
}
