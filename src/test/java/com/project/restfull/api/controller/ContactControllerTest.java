package com.project.restfull.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.ContactResponse;
import com.project.restfull.api.pojo.CreateContactRequest;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.repository.ContactRepo;
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
class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRepo.deleteAll();
        userRepo.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setName("test");
        user.setPassword("password");
        user.setToken("user-token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000L);
        userRepo.save(user);
    }

    @Test
    void createContactUnAuthorized() throws Exception {
        CreateContactRequest request = new CreateContactRequest();

        mockMvc.perform(
                post("http://localhost:8089/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setLastName("Acong");
        request.setEmail("fucking wrong email");
        request.setPhone("");

        mockMvc.perform(
                post("http://localhost:8089/api/contacts")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void createContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Ko");
        request.setLastName("Aliong");
        request.setEmail("fucking.aliong@example.com");
        request.setPhone("123456789");

        mockMvc.perform(
                post("http://localhost:8089/api/contacts")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals("Ko", response.getData().getFirstName());
            assertEquals("Aliong", response.getData().getLastName());
            assertEquals("fucking.aliong@example.com", response.getData().getEmail());
            assertEquals("123456789", response.getData().getPhone());

            assertTrue(contactRepo.existsById(response.getData().getId()));
        });
    }
}