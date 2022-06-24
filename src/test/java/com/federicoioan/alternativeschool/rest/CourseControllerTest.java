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
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String tutorToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpb2FuLWFkbWluIiwiaWF0IjoxNjU2MDc2NDY4LCJleHAiOjE2NTYxNjI4Njh9.WEzKrrJdMxE3ORkWApvwROsNFqTUcBFs_C0N6RENY64eFUNRqrmNMRykugbaUFkJ3Wyt0Gii-Cc8UIyvfxEOmQ";

    private String studentToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpb2FuLXN0dWRlbnQiLCJpYXQiOjE2NTYwNzY1NTgsImV4cCI6MTY1NjE2Mjk1OH0.CK-9-7ZXrih6sLKY1c7WjbNb8JImrrhqTbE-5GDHTX8kfS96Rr8oigdr_mTpRrXkpT9FIXO_3BKlfSm5Ivr2QQ";

    private Long courseId;

    private Long tutorId = 2L;

    private Long studentId = 3L;

    @Before
    public void setUp() throws Exception {
        SecurityContextHolder.clearContext();

        ResultActions resultAction = mockMvc.perform(post("/courses")
                        .header("Authorization", "Bearer " + tutorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"Test Course\"," +
                                "\"description\":\"Test Course Description\"," +
                                "\"ownerId\": 2" +
                                "}"))
                .andExpect(status().isOk());

        // Get response body
        String response = resultAction.andReturn().getResponse().getContentAsString();

        // Get course id from response
        courseId = Long.parseLong(response.substring(response.indexOf(":") + 1, response.indexOf(",")));
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    @Test
    @Order(1)
    public void getCourses() throws Exception {
        mockMvc.perform(get("/courses")
                .header("Authorization", "Bearer " + tutorToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(2)
    public void getCourse() throws Exception {
        mockMvc.perform(get("/courses/" + courseId)
                .header("Authorization", "Bearer " + tutorToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(3)
    public void enrollStudent() throws Exception {
        mockMvc.perform(post("/courses/" + courseId + "/enroll/" + studentId)
                .header("Authorization", "Bearer " + tutorToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void enableCertificate() throws Exception {
        mockMvc.perform(post("/courses/" + courseId + "/enable_certificate/" + studentId)
                .header("Authorization", "Bearer " + tutorToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void getCertificate() throws Exception {
        mockMvc.perform(get("/courses/" + courseId + "/certificate/" + studentId)
                .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void rateCourse() throws Exception {
        mockMvc.perform(post("/courses/" + courseId + "/rate")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"rating\":5" +
                        "}"))
                .andExpect(status().isOk());
    }
}
