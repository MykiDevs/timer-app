package org.ikitadevs.timerapp;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
public class AuthControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    private UserCreateDto userCreateDto;
    private MockHttpServletResponse response;
    @BeforeAll
    static void setUpAll() {
        log.info("Test is started!");
    }
    @AfterAll
    static void tearDownAll() {
        log.info("Test is ended!");
    }
    @BeforeEach
    void setUp(TestInfo testInfo) {
        log.info("Test {} started!", testInfo.getDisplayName());
        userRepository.deleteAll();
        userCreateDto = new UserCreateDto("Test", "test123", "test@te.st");
    }

    @AfterEach
    void tearDown(TestInfo testInfo) throws Exception {
        log.info("Test {} ended!", testInfo.getDisplayName());
        log.info("Response is: {}", response.getContentAsString());
        userRepository.findAll().stream().forEach(u -> log.info("User: {}", u));
    }

    @Test
    void createUser_withValidData_shouldReturn201AndUser() throws Exception {
        response = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andReturn().getResponse();
    }
    
}
