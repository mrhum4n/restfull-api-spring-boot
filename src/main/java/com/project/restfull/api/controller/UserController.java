package com.project.restfull.api.controller;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.UserBody;
import com.project.restfull.api.pojo.UserResponse;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody UserBody userBody) {
        userService.register(userBody);
        WebResponse<String> response = new WebResponse<>();
        response.setData("Ok");
        return response;
    }

    @GetMapping(path = "users/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> getUser(User user) {
        UserResponse userResponse = userService.getUser(user);
        WebResponse<UserResponse> response = new WebResponse<>();
        response.setData(userResponse);
        return response;
    }
}
