package com.project.restfull.api.service;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.UserBody;
import com.project.restfull.api.pojo.UserResponse;

public interface UserService {
    void register(UserBody userBody);
    UserResponse getUser(User user);
}
