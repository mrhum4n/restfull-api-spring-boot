package com.project.restfull.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.LoginBody;
import com.project.restfull.api.pojo.TokenResponse;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepo.deleteAll();
    }

    @Test
    void loginFailedUserNotFound() throws Exception {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("test");
        loginBody.setPassword("test");

        mockMvc.perform(
                post("http://localhost:8089/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void loginFailedWrongPassword() throws Exception {
        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword("password");
        userRepo.save(user);

        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("test");
        loginBody.setPassword("123456");

        mockMvc.perform(
                post("http://localhost:8089/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword("password");
        userRepo.save(user);

        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("test");
        loginBody.setPassword("password");

        mockMvc.perform(
                post("http://localhost:8089/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getExpiredAt());

            User userLocal = userRepo.findUserByUsername("test");
            assertNotNull(userLocal);
            assertEquals(userLocal.getToken(), response.getData().getToken());
            assertEquals(userLocal.getTokenExpiredAt(), response.getData().getExpiredAt());
        });
    }
}