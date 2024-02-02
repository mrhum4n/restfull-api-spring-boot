package com.project.restfull.api.service.impl;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.LoginBody;
import com.project.restfull.api.pojo.TokenResponse;
import com.project.restfull.api.repository.UserRepo;
import com.project.restfull.api.service.AuthService;
import com.project.restfull.api.service.ValidationService;
import com.project.restfull.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ValidationService validationService;

    @Override
    @Transactional
    public TokenResponse login(LoginBody loginBody) {
        validationService.validation(loginBody);

        User user = userRepo.findUserByUsername(loginBody.getUsername());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
        // VALIDATE PASSWORD
        if (loginBody.getPassword().equals(user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(Utils.next30Days());
            userRepo.save(user);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(user.getToken());
            tokenResponse.setExpiredAt(user.getTokenExpiredAt());

            return tokenResponse;
        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
        // VALIDATE PASSWORD
    }
}
