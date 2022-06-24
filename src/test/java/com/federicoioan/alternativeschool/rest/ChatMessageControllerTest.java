package com.federicoioan.alternativeschool.rest;


import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String tutorToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpb2FuLWFkbWluIiwiaWF0IjoxNjU2MDc2NDY4LCJleHAiOjE2NTYxNjI4Njh9.WEzKrrJdMxE3ORkWApvwROsNFqTUcBFs_C0N6RENY64eFUNRqrmNMRykugbaUFkJ3Wyt0Gii-Cc8UIyvfxEOmQ";

    private Long tutorId = 2L;

    private Long studentId = 3L;

    @Before
    public void setUp() throws Exception {
        SecurityContextHolder.clearContext();
    }

    @Test
    @Order(1)
    public void testSendMessage() throws Exception {
        mockMvc.perform(post("/chat/" + studentId)
                .header("Authorization", "Bearer " + tutorToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"message\":\"Test Message\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void getMessages() throws Exception {
        mockMvc.perform(get("/chat/" + studentId)
                .header("Authorization", "Bearer " + tutorToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void deleteMessage() throws Exception {
        mockMvc.perform(delete("/chat/" + studentId)
                .header("Authorization", "Bearer " + tutorToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
