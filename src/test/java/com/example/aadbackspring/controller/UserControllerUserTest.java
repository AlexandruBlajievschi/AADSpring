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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "regularuser", roles = {"user"})
public class UserControllerUserTest {

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
        // Clear the repository for a clean state
        userSubscriptionRepository.deleteAll();
        userRepository.deleteAll();

        // Add a sample user for GET tests
        User sample = new User();
        sample.setUsername("sampleuser");
        sample.setEmail("sampleuser@example.com");
        sample.setPassword(encoder.encode("samplepass"));
        sample.setRole("user");
        userRepository.save(sample);
    }

    // ---- GET ALL: Allowed for regular user ----
    @Test
    public void testGetAllUsers_AsUser_Success() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ---- GET BY ID: Allowed for regular user ----
    @Test
    public void testGetUserById_AsUser_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword(encoder.encode("testPass"));
        user.setRole("user");
        User saved = userRepository.save(user);

        mockMvc.perform(get("/users/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    // ---- CREATE: Forbidden for regular user ----
    @Test
    public void testCreateUser_AsUser_Forbidden() throws Exception {
        User user = new User();
        user.setUsername("unauthorized");
        user.setEmail("unauthorized@example.com");
        user.setPassword(encoder.encode("nopass"));
        user.setRole("user");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    // ---- UPDATE: Forbidden for regular user ----
    @Test
    public void testUpdateUser_AsUser_Forbidden() throws Exception {
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
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    // ---- DELETE: Forbidden for regular user ----
    @Test
    public void testDeleteUser_AsUser_Forbidden() throws Exception {
        User user = new User();
        user.setUsername("deleteuser");
        user.setEmail("deleteuser@example.com");
        user.setPassword(encoder.encode("deletepass"));
        user.setRole("user");
        User saved = userRepository.save(user);

        mockMvc.perform(delete("/users/{id}", saved.getId()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }
}
