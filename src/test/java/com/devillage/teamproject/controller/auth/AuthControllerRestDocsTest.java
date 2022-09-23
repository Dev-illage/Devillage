package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.ResponseDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.auth.AuthService;
import com.devillage.teamproject.util.ReflectionForStatic;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.devillage.teamproject.util.ReflectionForStatic.*;
import static com.devillage.teamproject.util.TestConstants.*;
import static com.devillage.teamproject.util.TestConstants.EMAIL1;
import static com.devillage.teamproject.util.TestConstants.ID1;
import static com.devillage.teamproject.util.auth.AuthTestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(
        value = {
                "jwt.secretKey=xptmxmdlsepeocndgkrpwhagownwldjfakskrlfrpTmfkrh",
                "jwt.refreshKey=dhkwlsWktlagkekdlfjgrpaksgdlTjdigksekrhwlsWkfhdlrpakwsi"
        }
)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AuthControllerRestDocsTest implements ReflectionForStatic {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void before() {
        jwtTokenUtil = new JwtTokenUtil(SECRET_KEY,REFRESH_KEY);
    }

    @Test
    @DisplayName("Post /auth/new RestDocs Test")
    public void test1() throws Exception {
        // given
        AuthDto.JOIN request = AuthDto.JOIN.builder()
                .email(EMAIL1)
                .password(PASSWORD1)
                .nickname(NICKNAME1)
                .build();

        User user = request.toEntity();
        String json = objectMapper.writeValueAsString(request);

        Long id = 1L;
        ReflectionTestUtils.setField(user,"id",id);

        when(authService.joinUser(any(User.class))).thenReturn(user);

        // when then
        mockMvc.perform(post("/auth/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(ID1.toString()))
                .andDo(
                        document("post-auth/new",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("user email"),
                                fieldWithPath("password").description("user password"),
                                fieldWithPath("nickname").description("user nickname")
                        ),
                        responseBody()
                        )
                );
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
                createToken(loginInfo.getEmail(),  ID1, ROLES, SECRET_KEY.getBytes(), SECRET_EXPIRE);
        String refreshToken =
                createToken(loginInfo.getEmail(),  ID1, ROLES, REFRESH_KEY.getBytes(), SECRET_EXPIRE);
        AuthDto.Token tokenDto = AuthDto.Token.of(BEARER, accessToken, refreshToken);

        when(authService.loginUser(any(User.class))).thenReturn(tokenDto);

        ResponseDto.SingleResponseDto<AuthDto.Token> response = ResponseDto.SingleResponseDto.of(tokenDto);
        String json = objectMapper.writeValueAsString(loginInfo);
        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                .andExpect(jsonPath("$.data.bearer").value(BEARER))
                .andDo(document(
                        "post-auth/token",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("user email"),
                                fieldWithPath("password").description("user password")
                        ),
                        responseFields(
                                List.of(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("data container"),
                                fieldWithPath("data.bearer").type(JsonFieldType.STRING).description("authorization header prefix"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("accessToken"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("refreshToken")
                                )
                        )
                )
                );
    }

    @Test
    @DisplayName("POST /auth/token/refresh 200 successTest")
    public void test4() throws Exception {
        //given
        String request = jwtTokenUtil.createRefreshToken(EMAIL1, ID1, ROLES);

        String refreshToken = jwtTokenUtil.createRefreshToken(EMAIL1, ID1, ROLES);
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, ID1, ROLES);

        AuthDto.Token response = AuthDto.Token.of(BEARER, accessToken, refreshToken);

        when(authService.reIssue(request)).thenReturn(response);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token/refresh")
                        .header("refreshToken", request))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.bearer").value(BEARER))
                .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                .andDo(
                        document("post-auth/token/refresh",
                                preprocessRequest(),
                                preprocessResponse(),
                                requestHeaders(
                                        headerWithName("refreshToken").description("existing refresh token")
                                ),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("data container"),
                                                fieldWithPath("data.bearer").type(JsonFieldType.STRING).description("authorization header prefix"),
                                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("accessToken"),
                                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("refreshToken")
                                        )
                                )
                                )
                );
    }
}
