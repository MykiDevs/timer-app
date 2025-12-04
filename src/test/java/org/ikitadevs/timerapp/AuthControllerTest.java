package org.ikitadevs.timerapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.configs.SecurityConfiguration;
import org.ikitadevs.timerapp.controllers.AuthController;
import org.ikitadevs.timerapp.controllers.UserController;
import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.entities.enums.Role;
import org.ikitadevs.timerapp.filters.JwtAuthFilter;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.services.JwtService;
import org.ikitadevs.timerapp.services.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AuthController.class)
@Slf4j
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserMapper userMapper;

    private final UUID user_uuid = UUID.randomUUID();
    private UserCreateDto userCreateDto;
    private UserResponseDto userResponseDto;
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
    void setUp() {
        userCreateDto = new UserCreateDto("Name", "password", "test@te.st");
        userResponseDto = new UserResponseDto(
                user_uuid,
                1L,
                userCreateDto.getName(),
                userCreateDto.getEmail(),
                null,
                null,
                null
        );
    }

    @AfterEach
    void tearDown(TestInfo testInfo) throws Exception {
        log.info("Execution of test with {} method is eneded: ", testInfo.getDisplayName());
        log.info("UserCreateDto is: {}", objectMapper.writeValueAsString(userCreateDto));
        log.info("Response is: {}", response.getContentAsString());
        log.info("Test is success!");
    }
    @Test
    void signupUser_withValidData_shouldReturn201AndUser()  {
        given(userService.createUser(any(UserCreateDto.class))).willReturn(userResponseDto);
        try {
            response = mockMvc.perform(post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCreateDto))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.uuid").value(user_uuid.toString()))
                    .andExpect(jsonPath("$.email").value("test@te.st"))
                    .andReturn().getResponse();
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
        }
    }
    @Test
    void signupUser_withInvalidData_shouldReturn400AndError()  {
        userCreateDto.setEmail("testte.st");
        userCreateDto.setName("idorpflskdpoiiiaoopsppdpkdkpapp");
        userCreateDto.setPassword(null);
        given(userService.createUser(any(UserCreateDto.class))).willReturn(userResponseDto);
        try {
             response = mockMvc.perform(post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userCreateDto))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.errorsList").isMap())
                    .andReturn().getResponse();
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
        }
    }

}
