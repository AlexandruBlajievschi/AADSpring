package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.User;
import com.example.aadbackspring.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Clear the repository for a clean state before each test
        userRepository.deleteAll();
    }

    // ---- CREATE ----
    @Test
    public void testCreateUser_Success() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        user.setRole("user");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("johndoe")))
                .andExpect(jsonPath("$.email", is("johndoe@example.com")));
    }

    // ---- GET ALL ----
    @Test
    public void testGetAllUsers_Success() throws Exception {
        // Create a sample user
        User user = new User();
        user.setUsername("janedoe");
        user.setEmail("janedoe@example.com");
        user.setPassword("securePass");
        user.setRole("admin");
        userRepository.save(user);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ---- GET BY ID ----
    @Test
    public void testGetUserById_Success() throws Exception {
        // Create and save a user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("testPass");
        user.setRole("user");
        User saved = userRepository.save(user);

        mockMvc.perform(get("/users/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        // Using an ID that doesn't exist
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                // Assuming your controller returns an "error" field in the JSON when not found:
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    // ---- UPDATE ----
    @Test
    public void testUpdateUser_Success() throws Exception {
        // Create and save a user
        User user = new User();
        user.setUsername("oldusername");
        user.setEmail("old@example.com");
        user.setPassword("oldpass");
        user.setRole("user");
        User saved = userRepository.save(user);

        // Prepare update data
        User updateData = new User();
        updateData.setUsername("newusername");
        updateData.setEmail("new@example.com");
        updateData.setPassword("newpass");
        updateData.setRole("admin");

        mockMvc.perform(put("/users/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("newusername")))
                .andExpect(jsonPath("$.email", is("new@example.com")))
                .andExpect(jsonPath("$.role", is("admin")));
    }

    @Test
    public void testUpdateUser_NotFound() throws Exception {
        User updateData = new User();
        updateData.setUsername("nonexistent");
        updateData.setEmail("nonexistent@example.com");
        updateData.setPassword("nopass");
        updateData.setRole("user");

        mockMvc.perform(put("/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    // ---- DELETE ----
    @Test
    public void testDeleteUser_Success() throws Exception {
        // Create and save a user
        User user = new User();
        user.setUsername("deleteuser");
        user.setEmail("deleteuser@example.com");
        user.setPassword("deletepass");
        user.setRole("user");
        User saved = userRepository.save(user);

        // Delete user
        mockMvc.perform(delete("/users/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("deleted")));

        // Verify deletion: attempt to fetch the user should return 404
        mockMvc.perform(get("/users/{id}", saved.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()));
    }
}