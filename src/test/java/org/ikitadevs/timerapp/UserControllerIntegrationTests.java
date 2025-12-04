package org.ikitadevs.timerapp;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.entities.enums.Role;
import org.ikitadevs.timerapp.repositories.UserRepository;
import org.ikitadevs.timerapp.services.FileStorageService;
import org.ikitadevs.timerapp.services.JwtService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockHttpServletResponse response;
    private User testUser;
    private String userJwtToken;
    @AfterAll
    static void tearDownAll() {
        log.info("Test is ended!");
    }

    @BeforeAll
    static void setUpAll() {
        log.info("Test is started!");
        log.info("test");
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws Exception {
        log.info("Test {} started!", testInfo.getDisplayName());
        userRepository.deleteAll();

        testUser.setName("Test User");
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setActive(true);
        testUser.setAvatar(null);
        testUser.setRoles(new HashSet<>(Set.of(Role.ROLE_USER)));
        testUser = userRepository.save(testUser);
        userJwtToken = jwtService.generateAccessToken(testUser);
        log.info("Response content is: {}", response.getContentAsString());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        log.info("Test {} ended!", testInfo.getDisplayName());
    }

    @Test
    void updateOrCreateAvatar_shouldReturn200AndOk() throws Exception {
        ClassPathResource imgFile = new ClassPathResource("static/images/defaut_profile.png");
        byte[] imageBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        String expectedFilePath = "avatars/some-generated-path.png";
        when(fileStorageService.storeFile(any(), eq("avatar"))).thenReturn(expectedFilePath);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("avatar", "test.png", MediaType.IMAGE_PNG_VALUE, imageBytes);
        response = mockMvc.perform(multipart("/api/users/profile/avatar")
                .file(mockMultipartFile)
                .header("Authorization", "Bearer " + userJwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").isString())
                .andReturn().getResponse();
    }
}
