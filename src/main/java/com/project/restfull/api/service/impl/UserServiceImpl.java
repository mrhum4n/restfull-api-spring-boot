package com.project.restfull.api.service.impl;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.UserRequest;
import com.project.restfull.api.pojo.UserResponse;
import com.project.restfull.api.pojo.UserUpdateRequest;
import com.project.restfull.api.repository.UserRepo;
import com.project.restfull.api.service.UserService;
import com.project.restfull.api.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ValidationService validationService;

    /*@Autowired
    private PasswordEncoder passwordEncoder;*/

    @Transactional
    @Override
    public void register(UserRequest userRequest) {
        validationService.validation(userRequest);

        // VALIDATION USERNAME
        User userValidation = userRepo.findUserByUsername(userRequest.getUsername());
        if (userValidation != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered!");
        }
        // VALIDATION USERNAME

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setUsername(userRequest.getName());
//        user.setPassword(passwordEncoder.encode(userBody.getPassword()));
        user.setPassword(userRequest.getPassword());
        userRepo.save(user);
    }

    @Override
    public UserResponse getUser(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setName(user.getName());
        return userResponse;
    }

    @Override
    @Transactional
    public UserResponse updateUser(User user, UserUpdateRequest userUpdateRequest) {
        if (Objects.nonNull(userUpdateRequest.getName())) {
            user.setName(userUpdateRequest.getName());
        }
        if (Objects.nonNull(userUpdateRequest.getPassword())) {
            user.setName(userUpdateRequest.getPassword());
        }
        userRepo.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setUsername(user.getUsername());

        return userResponse;
    }
}
