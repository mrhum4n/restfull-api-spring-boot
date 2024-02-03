package com.project.restfull.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.UserBody;
import com.project.restfull.api.pojo.UserResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
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
    void testRegisterSuccess() throws Exception {
        UserBody userBody = new UserBody();
        userBody.setUsername("test");
        userBody.setName("test");
        userBody.setPassword("123456");

        mockMvc.perform(
                post("http://localhost:8089/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBody))
        ).andExpectAll(
                status().isOk()
        ).andDo(result ->{
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("Ok", response.getData());
        });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        UserBody userBody = new UserBody();
        userBody.setUsername("");
        userBody.setName("");
        userBody.setPassword("");

        mockMvc.perform(
                post("http://localhost:8089/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBody))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result ->{
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("test");
        user.setPassword("password");
        userRepo.save(user);

        UserBody userBody = new UserBody();
        userBody.setUsername("test");
        userBody.setName("test");
        userBody.setPassword("password");

        mockMvc.perform(
                post("http://localhost:8089/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBody))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result ->{
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getUserUnauthorizedInvalidToken() throws Exception {
        mockMvc.perform(
                get("http://localhost:8089/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN", "test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getUserUnauthorizedTokenNotSend() throws Exception {
        mockMvc.perform(
                get("http://localhost:8089/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("test");
        user.setPassword("password");
        user.setToken("test-token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000L);
        userRepo.save(user);

        mockMvc.perform(
                get("http://localhost:8089/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN", user.getToken())
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals("test", response.getData().getUsername());
            assertEquals("test", response.getData().getName());
        });
    }

    @Test
    void getUserTokenExpired() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("test");
        user.setPassword("password");
        user.setToken("test-token");
        user.setTokenExpiredAt(System.currentTimeMillis() - 100000L);
        userRepo.save(user);

        mockMvc.perform(
                get("http://localhost:8089/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN", user.getToken())
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }
}