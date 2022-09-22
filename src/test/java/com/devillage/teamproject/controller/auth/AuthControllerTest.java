package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.ResponseDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.service.auth.AuthService;
import com.devillage.teamproject.util.ReflectionForStatic;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static com.devillage.teamproject.util.ReflectionForStatic.*;
import static com.devillage.teamproject.util.TestConstants.*;
import static com.devillage.teamproject.util.auth.AuthTestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(value = {
        "jwt.secretKey=xptmxmdlsepeocndgkrpwhagownwldjfakskrlfrpTmfkrh",
        "jwt.refreshKey=dhkwlsWktlagkekdlfjgrpaksgdlTjdigksekrhwlsWkfhdlrpakwsi"
})
@AutoConfigureMockMvc
public class AuthControllerTest implements ReflectionForStatic {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("POST /auth/new 200 Success Test")
    public void test1() throws Exception {
        // given
        AuthDto.JOIN request = AuthDto.JOIN.builder()
                .email(EMAIL1)
                .password(PASSWORD1)
                .nickname(NICKNAME1)
                .build();

        User user = request.toEntity();
        ReflectionTestUtils.setField(user,"id",ID1);

        String json = objectMapper.writeValueAsString(request);

        when(authService.joinUser(any(User.class))).thenReturn(user);

        // when then
        mockMvc.perform(post("/auth/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(ID1.toString()))
                .andDo(print());
    }

    @Test
    @DisplayName("POST /auth/new 400 Failure Test" +
            "기대값이 입력값과 다를 경우 실패한다.")
    public void test2() throws Exception {
        // given
        AuthDto.JOIN request = AuthDto.JOIN.builder()
                .email(EMAIL1)
                .password(PASSWORD1)
                .nickname(NICKNAME1)
                .build();

        User user = request.toEntity();
        ReflectionTestUtils.setField(user,"id",ID1);

        String json = objectMapper.writeValueAsString(request);
        when(authService.joinUser(any(User.class))).thenReturn(user);

        // when then
        MvcResult result = mockMvc.perform(post("/auth/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isNotEqualTo(WRONG_ID.toString());
    }

    @Test
    @DisplayName("Post /auth/token 200 success Test")
    public void test3() throws Exception {
        // given
        AuthDto.Login loginInfo = AuthDto.Login.builder()
                .email(EMAIL1)
                .password(PASSWORD1)
                .build();
        User user = loginInfo.toEntity();
        setField(user, "id", ID1);

        String accessToken =
                createToken(loginInfo.getEmail(), Collections.singletonList("ROLE_USER"), SECRET_EXPIRE, SECRET_KEY, ID1);
        String refreshToken =
                createToken(loginInfo.getEmail(), null, REFRESH_EXPIRE, REFRESH_KEY, ID1);
        AuthDto.Token tokenDto = AuthDto.Token.of(BEARER, accessToken, refreshToken);

        when(authService.loginUser(any(User.class))).thenReturn(tokenDto);

        ResponseDto.SingleResponseDto<AuthDto.Token> response = ResponseDto.SingleResponseDto.of(tokenDto);
        String json = objectMapper.writeValueAsString(loginInfo);
        // when
        ResultActions perform = mockMvc.perform(post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                .andExpect(jsonPath("$.data.bearer").value(BEARER));
    }
}
