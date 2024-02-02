package com.project.restfull.api.controller;

import com.project.restfull.api.pojo.UserBody;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class RootController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody UserBody userBody) {
        userService.register(userBody);
        WebResponse<String> response = new WebResponse<>();
        response.setData("Ok");
        return response;
    }
}
