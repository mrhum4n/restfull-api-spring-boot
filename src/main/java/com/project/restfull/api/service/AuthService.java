package com.project.restfull.api.service;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.LoginRequest;
import com.project.restfull.api.pojo.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest loginRequest);
    void logout(User user);
}
