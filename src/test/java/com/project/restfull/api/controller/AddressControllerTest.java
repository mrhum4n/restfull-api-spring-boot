package com.project.restfull.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.restfull.api.model.Address;
import com.project.restfull.api.model.Contact;
import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.AddressResponse;
import com.project.restfull.api.pojo.CreateAddressRequest;
import com.project.restfull.api.pojo.UpdateAddressRequest;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.repository.AddressRepo;
import com.project.restfull.api.repository.ContactRepo;
import com.project.restfull.api.repository.UserRepo;
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
class AddressControllerTest {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        addressRepo.deleteAll();
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
    void createBadRequest() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                post("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses")
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
    void createSuccess() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Jalan");
        request.setCity("Punk");
        request.setProvince("Bali");
        request.setCountry("Bastrad");
        request.setPostalCode("Shit");

        mockMvc.perform(
                post("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());
            assertTrue(addressRepo.existsById(response.getData().getId()));
        });
    }

    @Test
    void addressNotFound() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                get("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses/test")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getAddressNotFound() throws Exception {
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
                get("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses/test")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getAddressSuccess() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        Address address = new Address();
        address.setStreet("Jalan");
        address.setCity("Kota");
        address.setProvince("Provinsi");
        address.setCountry("Negara");
        address.setPostalCode("123");
        address.setContact(contact);
        addressRepo.save(address);

        mockMvc.perform(
                get("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses/"+ address.getId())
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals(address.getId(), response.getData().getId());
            assertEquals(address.getStreet(), response.getData().getStreet());
            assertEquals(address.getCity(), response.getData().getCity());
            assertEquals(address.getProvince(), response.getData().getProvince());
            assertEquals(address.getCountry(), response.getData().getCountry());
            assertEquals(address.getPostalCode(), response.getData().getPostalCode());
        });
    }

    @Test
    void updateBadRequest() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        Address address = new Address();
        address.setStreet("Jalan");
        address.setCity("Kota");
        address.setProvince("Provinsi");
        address.setCountry("Negara");
        address.setPostalCode("123");
        address.setContact(contact);
        addressRepo.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                put("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses/"+ address.getId())
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
    void updateSuccess() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        Address address = new Address();
        address.setStreet("Jalan");
        address.setCity("Kota");
        address.setProvince("Provinsi");
        address.setCountry("Negara");
        address.setPostalCode("123");
        address.setContact(contact);
        addressRepo.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Jl. mawar");
        request.setCity("Denpasar");
        request.setProvince("Bali");
        request.setCountry("Indonesia");
        request.setPostalCode("123");

        mockMvc.perform(
                put("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses/"+ address.getId())
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());
        });
    }

    @Test
    void deleteNotFound() throws Exception {
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
                delete("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses/test")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void deleteSuccess() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        Address address = new Address();
        address.setStreet("Jalan");
        address.setCity("Kota");
        address.setProvince("Provinsi");
        address.setCountry("Negara");
        address.setPostalCode("123");
        address.setContact(contact);
        addressRepo.save(address);

        mockMvc.perform(
                delete("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses/"+ address.getId())
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals("Ok", response.getData());
            assertFalse(addressRepo.existsById(address.getId()));
        });
    }

    @Test
    void listAddressNotFound() throws Exception {
        mockMvc.perform(
                get("http://localhost:8089/api/contact/salah/addresses")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void listAddressSuccess() throws Exception {
        User user = userRepo.findUserByUsername("test");
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setFirstName("Mr");
        contact.setLastName("Shit man");
        contact.setEmail("shitman@example.com");
        contact.setPhone("666");
        contact.setUser(user);
        contactRepo.save(contact);

        for (int i = 0; i < 5; i++) {
            Address address = new Address();
            address.setStreet("Jalan"+ i);
            address.setCity("Kota");
            address.setProvince("Provinsi");
            address.setCountry("Negara");
            address.setPostalCode("123");
            address.setContact(contact);
            addressRepo.save(address);
        }

        mockMvc.perform(
                get("http://localhost:8089/api/contact/"+ contact.getId() +"/addresses")
                        .header("X-TOKEN", "user-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals(5, response.getData().size());
        });
    }
}