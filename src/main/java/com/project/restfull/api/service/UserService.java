package com.project.restfull.api.service;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.UserRequest;
import com.project.restfull.api.pojo.UserResponse;
import com.project.restfull.api.pojo.UserUpdateRequest;

public interface UserService {
    void register(UserRequest userRequest);
    UserResponse getUser(User user);
    UserResponse updateUser(User user, UserUpdateRequest userUpdateRequest);
}
