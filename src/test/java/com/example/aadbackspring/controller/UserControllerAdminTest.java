package com.example.aadbackspring.controller;

import com.example.aadbackspring.config.PasswordEncoderUtil;
import com.example.aadbackspring.model.User;
import com.example.aadbackspring.repository.UserRepository;
import com.example.aadbackspring.repository.stripe.UserSubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
public class UserControllerAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final PasswordEncoderUtil encoder = new PasswordEncoderUtil();

    @BeforeEach
    public void setup() {
        // Clear the repository to ensure a clean test environment
        userSubscriptionRepository.deleteAll();
        userRepository.deleteAll();
    }

    // ---- CREATE ----
    @Test
    public void testCreateUser_AsAdmin_Success() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword(encoder.encode("password123"));
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
    public void testGetAllUsers_AsAdmin_Success() throws Exception {
        User user = new User();
        user.setUsername("janedoe");
        user.setEmail("janedoe@example.com");
        user.setPassword(encoder.encode("securePass"));
        user.setRole("admin");
        userRepository.save(user);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ---- GET BY ID ----
    @Test
    public void testGetUserById_AsAdmin_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword(encoder.encode("testPass"));
        user.setRole("user");
        User saved = userRepository.save(user);

        mockMvc.perform(get("/users/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    @Test
    public void testGetUserById_AsAdmin_NotFound() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    // ---- UPDATE ----
    @Test
    public void testUpdateUser_AsAdmin_Success() throws Exception {
        User user = new User();
        user.setUsername("oldusername");
        user.setEmail("old@example.com");
        user.setPassword(encoder.encode("oldpass"));
        user.setRole("user");
        User saved = userRepository.save(user);

        User updateData = new User();
        updateData.setUsername("newusername");
        updateData.setEmail("new@example.com");
        updateData.setPassword(encoder.encode("newpass"));
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
    public void testUpdateUser_AsAdmin_NotFound() throws Exception {
        User updateData = new User();
        updateData.setUsername("nonexistent");
        updateData.setEmail("nonexistent@example.com");
        updateData.setPassword(encoder.encode("nopass"));
        updateData.setRole("user");

        mockMvc.perform(put("/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    // ---- DELETE ----
    @Test
    public void testDeleteUser_AsAdmin_Success() throws Exception {
        User user = new User();
        user.setUsername("deleteuser");
        user.setEmail("deleteuser@example.com");
        user.setPassword(encoder.encode("deletepass"));
        user.setRole("user");
        User saved = userRepository.save(user);

        mockMvc.perform(delete("/users/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("deleted")));

        mockMvc.perform(get("/users/{id}", saved.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    @Test
    public void testDeleteUser_AsAdmin_NotFound() throws Exception {
        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()));
    }
}
