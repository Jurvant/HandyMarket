package ilya.pon.profile.controller;

import ilya.pon.profile.configuration.security.CustomWebSecurityConfigurerAdapter;
import ilya.pon.profile.dto.LoginDto;
import ilya.pon.profile.dto.RegisterDto;
import ilya.pon.profile.dto.ResponseLoginDto;
import ilya.pon.profile.service.LoginService;
import ilya.pon.profile.service.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProfileController.class)
@Import(CustomWebSecurityConfigurerAdapter.class) //Security filter chain
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RegisterService registerService;

    @MockitoBean
    LoginService loginService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void registerNewProfile() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "firstName",
                "lastName",
                "email@gmail.com",
                "username",
                "password"
        );

        mockMvc.perform(post("/profile-service/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

    }

    @Test
    void loginTest() throws Exception {
        LoginDto loginDto = new LoginDto(
                "username",
                "password"
        );

        ResponseLoginDto returned = new ResponseLoginDto(
                "accessToken",
                "refreshToken",
                1L,
                "tokenType"
        );
        when(loginService.login(loginDto)).thenReturn(returned);

        mockMvc.perform(get("/profile-service/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }
}