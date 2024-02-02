package com.project.restfull.api.controller;

import com.project.restfull.api.pojo.LoginBody;
import com.project.restfull.api.pojo.TokenResponse;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(path = "auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TokenResponse> login(@RequestBody LoginBody loginBody) {
        TokenResponse tokenResponse = authService.login(loginBody);
        WebResponse<TokenResponse> webResponse = new WebResponse<>();
        webResponse.setData(tokenResponse);
        return webResponse;
    }
}
