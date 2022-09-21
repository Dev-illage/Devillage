package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("POST /auth/new 200 Success Test")
    public void test1() throws Exception {
        // given
        String email = "email@test.com";
        String password = "testPassword!23";
        String nickname = "코딩잘하고싶다";
        Long id = 1L;

        AuthDto.JOIN request = AuthDto.JOIN.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        User user = request.toEntity();
        ReflectionTestUtils.setField(user,"id",id);

        String json = objectMapper.writeValueAsString(request);

        when(authService.joinUser(any(User.class))).thenReturn(user);

        // when then
        mockMvc.perform(post("/auth/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(id.toString()))
                .andDo(print());
    }

    @Test
    @DisplayName("POST /auth/new 400 Failure Test" +
            "기대값이 2일 경우 실패한다.")
    public void test2() throws Exception {
        // given
        String email = "email@test.com";
        String password = "testPassword!23";
        String nickname = "코딩잘하고싶다";
        Long id = 1L;
        Long wrongId = 2L;

        AuthDto.JOIN request = AuthDto.JOIN.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        User user = request.toEntity();
        ReflectionTestUtils.setField(user,"id",id);

        String json = objectMapper.writeValueAsString(request);

        when(authService.joinUser(any(User.class))).thenReturn(user);

        // when then
        MvcResult result = mockMvc.perform(post("/auth/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isNotEqualTo(wrongId.toString());
    }
}
