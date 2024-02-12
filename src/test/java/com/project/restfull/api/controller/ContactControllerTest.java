package com.project.restfull.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.restfull.api.model.Contact;
import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.ContactResponse;
import com.project.restfull.api.pojo.CreateContactRequest;
import com.project.restfull.api.pojo.UpdateContactRequest;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.repository.ContactRepo;
import com.project.restfull.api.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
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

    @Test
    void getContactNotFound() throws Exception {
        mockMvc.perform(
                get("http://localhost:8089/api/contacts/123")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getContactSuccess() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        mockMvc.perform(
                get("http://localhost:8089/api/contacts/" + contact.getId())
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals(contact.getId(), response.getData().getId());
            assertEquals(contact.getFirstName(), response.getData().getFirstName());
            assertEquals(contact.getLastName(), response.getData().getLastName());
            assertEquals(contact.getEmail(), response.getData().getEmail());
            assertEquals(contact.getPhone(), response.getData().getPhone());
        });
    }

    @Test
    void updateContactNotFound() throws Exception {
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("hihi");
        request.setLastName("Acong");
        request.setEmail("example@example.com");
        request.setPhone("123");

        mockMvc.perform(
                put("http://localhost:8089/api/contacts/123")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateContactBadRequest() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("");
        request.setLastName("Acong");
        request.setEmail("fucking wrong email");
        request.setPhone("");

        mockMvc.perform(
                put("http://localhost:8089/api/contacts/" + contact.getId())
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
    void updateContactSuccess() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("kleg");
        request.setLastName("ci");
        request.setEmail("kleg.ci@example.com");
        request.setPhone("696969");

        mockMvc.perform(
                put("http://localhost:8089/api/contacts/" + contact.getId())
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
            assertEquals(request.getFirstName(), response.getData().getFirstName());
            assertEquals(request.getLastName(), response.getData().getLastName());
            assertEquals(request.getEmail(), response.getData().getEmail());
            assertEquals(request.getPhone(), response.getData().getPhone());
        });
    }

    @Test
    void deleteContactNotFound() throws Exception {
        mockMvc.perform(
                delete("http://localhost:8089/api/contacts/123")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void deleteContactSuccess() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        mockMvc.perform(
                delete("http://localhost:8089/api/contacts/" + contact.getId())
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals("Ok", response.getData());
            assertFalse(contactRepo.existsById(contact.getId()));
        });
    }

    @Test
    void searchContacts() throws Exception {
        mockMvc.perform(
                get("http://localhost:8089/api/contacts")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            assertEquals(0, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(5, response.getPaging().getSize());
        });
    }

    @Test
    void searchByName() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setFirstName("Mr");
            contact.setLastName("Shit man "+ i);
            contact.setEmail("shitman@example.com");
            contact.setPhone("666");
            contact.setUser(user);
            contactRepo.save(contact);
        }

        mockMvc.perform(
                get("http://localhost:8089/api/contacts")
                        .queryParam("name", "man")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            log.info("size: {} totalPage: {}, currentPage: {}", response.getPaging().getTotalPage(), response.getPaging().getCurrentPage(), response.getPaging().getSize());
            assertEquals(5, response.getData().size());
            assertEquals(20, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(5, response.getPaging().getSize());
        });
    }
}