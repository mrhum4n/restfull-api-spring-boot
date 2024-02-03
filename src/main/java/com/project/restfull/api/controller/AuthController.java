package com.project.restfull.api.controller;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.LoginRequest;
import com.project.restfull.api.pojo.TokenResponse;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        WebResponse<TokenResponse> webResponse = new WebResponse<>();
        webResponse.setData(tokenResponse);
        return webResponse;
    }

    @DeleteMapping(path = "auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> logout(User user) {
        authService.logout(user);
        WebResponse<String> webResponse = new WebResponse<>();
        webResponse.setData("Ok");
        return webResponse;
    }
}
