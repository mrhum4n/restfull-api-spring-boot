package com.project.restfull.api.service;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.LoginBody;
import com.project.restfull.api.pojo.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginBody loginBody);
    void logout(User user);
}
