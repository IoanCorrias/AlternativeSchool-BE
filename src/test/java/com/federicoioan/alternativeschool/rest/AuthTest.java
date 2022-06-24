package com.federicoioan.alternativeschool.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    @Test
    public void testLoginSuccessful() throws Exception {
        getMockMvc().perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"ioan-admin\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")));
    }

    @Test
    public void testLoginFail() throws Exception {
        getMockMvc().perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"ioan-admin\", \"password\": \"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegistrationSuccessful() throws Exception {
        getMockMvc().perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\": \"Jon\", " +
                                "\"surname\": \"Dorian\", " +
                                "\"email\": \"jon.dorian@gmail.com\", " +
                                "\"username\": \"jon-dorian\", " +
                                "\"password\": \"password\"," +
                                "\"roles\": [\"student\"]" +

                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegistrationFail () throws Exception {
        getMockMvc().perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\": \"Jon\", " +
                                "\"surname\": \"Dorian\", " +
                                "\"username\": \"jon-dorian\", " +
                                "\"password\": \"password\"," +
                                "\"roles\": [\"superadmin\"]" +

                                "}"))
                .andExpect(status().isOk());
    }
}
