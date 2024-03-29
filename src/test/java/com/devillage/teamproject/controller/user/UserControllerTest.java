package com.devillage.teamproject.controller.user;


import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.security.config.SecurityConfig;
import com.devillage.teamproject.security.resolver.ResultJwtArgumentResolver;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.file.FileService;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import com.devillage.teamproject.util.TestConstants;
import com.devillage.teamproject.util.auth.AuthTestUtils;
import com.devillage.teamproject.util.security.SecurityTestConfig;
import com.devillage.teamproject.util.security.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.PostConstruct;

import static com.devillage.teamproject.security.util.JwtConstants.*;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static com.devillage.teamproject.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {UserController.class, JwtTokenUtil.class})
@WithMockCustomUser
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@Slf4j
class UserControllerTest implements Reflection {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private FileService fileService;


    @Test
    @DisplayName("getProfile")
    public void getProfile() throws Exception {
        // given
        User user = newInstance(User.class);
        setField(user, "id", ID1);
        setField(user, "email", EMAIL1);
        setField(user, "nickName", NICKNAME1);
        setField(user, "statusMessage", STATUS_MESSAGE1);
        setField(user, "pwdLastModifiedAt", PASSWORD_LAST_MODIFIED_AT1);

        given(userService.findUser(anyString())).willReturn(user);

        // when
        ResultActions actions = mockMvc.perform(
                get("/users/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, "some-token")
        );

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.nickname").value(user.getNickName()))
                .andExpect(jsonPath("$.statusMessage").value(user.getStatusMessage()))
                .andDo(document(
                        "get-user-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("jwt 토큰")
                        ),
                        responseFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("statusMessage").description("상태메시지"),
                                fieldWithPath("passwordModifiedAt").description("최근 암호 수정날짜"),
                                fieldWithPath("avatar").type(JsonFieldType.OBJECT).description("프로필사진 정보"),
                                fieldWithPath("avatar.fileId").description("프로필사진 식별자"),
                                fieldWithPath("avatar.filename").description("프로필사진 파일 이름"),
                                fieldWithPath("avatar.remotePath").description("프로필사진 원격 줏소")
                        )
                ))
                .andReturn();

        String actualPasswordModifiedAt = JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.passwordModifiedAt").toString();

        assertEquals(user.getPwdLastModifiedAt().toString().substring(0, 19),
                actualPasswordModifiedAt.substring(0, 19));

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("deleteUser")
    public void deleteUser() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(
                delete("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, "some-token")
        );

        // then
        actions.andExpect(status().isNoContent())
                .andDo(document(
                        "delete-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("jwt 토큰")
                        )
                ));

    }

    @Test
    @DisplayName("blockUser")
    public void blockUser() throws Exception {
        // given
        User srcUser = newInstance(User.class);
        setField(srcUser, "id", ID1);
        User targetUser = newInstance(User.class);
        setField(targetUser, "id", ID2);
        Block block = Block.builder().srcUser(srcUser).destUser(targetUser).id(ID1).build();

        given(userService.blockUser(anyLong(), anyString())).willReturn(block);

        // when
        ResultActions actions = mockMvc.perform(
                post("/users/block/{target-user-id}", targetUser.getId())
                        .header(AUTHORIZATION_HEADER, "some_token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.blockId").value(block.getId()))
                .andExpect(jsonPath("$.data.srcUserId").value(block.getSrcUser().getId()))
                .andExpect(jsonPath("$.data.targetUserId").value(block.getDestUser().getId()))
                .andDo(document(
                        "block-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("jwt 토큰")
                        ),
                        pathParameters(
                                parameterWithName("target-user-id").description("블락할 회원 식별자")
                        ),
                        responseFields(
                                fieldWithPath("data").description("결과 데이터"),
                                fieldWithPath("data.blockId").description("블락 식별자"),
                                fieldWithPath("data.srcUserId").description("블락을 요청한 회원 식별자"),
                                fieldWithPath("data.targetUserId").description("블락을 당하는 회원 식별자")
                        )
                ));

    }

//    @Test
//    @DisplayName("Post /user/profile/{id} 테스트")
//    public void userPasswordVerifiedTest() throws Exception {
//        //given
//        User testUser = AuthTestUtils.createTestUser(EMAIL1, NICKNAME1, PASSWORD1);
//        given(userService.checkUserPassword(anyLong(), anyString(), anyLong())).willReturn(ID1);
//
//        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);
//
//        String password = PASSWORD1;
//        String json = objectMapper.writeValueAsString(password);
//
//        //when
//        ResultActions actions = mockMvc.perform(post("/users/profile/{id}", ID1)
//                .header(AUTHORIZATION_HEADER, token)
//                .content(json));
//
//        //then
//        actions.andExpect(status().isOk())
//                .andDo(
//                        document("post-user/profile",
//                                preprocessRequest(prettyPrint()),
//                                preprocessResponse(prettyPrint()),
//                                requestHeaders(
//                                        headerWithName(AUTHORIZATION_HEADER).description("Access Token")
//                                ),
//                                pathParameters(
//                                        parameterWithName("id").description("user key")
//                                ),
//                                responseBody())
//                );
//    }

//    @Test
//    public void updatePassword() throws Exception{
//        UserDto.PasswordDto passwordDto = UserDto.PasswordDto.builder().build();
//        User user = newInstance(User.class);
//        setField(user,"id",ID1);
//        setField(user,"email",EMAIL2);
//        setField(user,"nickName",NICKNAME1);
//        setField(user,"password",PASSWORD1);
//        AuthDto.UserInfo userInfo = AuthDto.UserInfo.builder().id(user.getId()).build();
//
//        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL2, ID1, TestConstants.ROLES);
//
//        Long userId = user.getId();
//        String updatePassword = "aasssssad##!!";
//
//        String content = objectMapper.writeValueAsString(passwordDto);
//
//        when(userService.updatePassword(userInfo,user.getPassword(),updatePassword)).thenReturn(true);
//
//        // when
//        ResultActions actions = mockMvc.perform(
//                patch("/users/pwd/{user-id}", userId)
//                        .header(AUTHORIZATION_HEADER, token)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content)
//        );
//
//        // then
//        MvcResult result = actions
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"))
//                .andReturn();
//    }

    @Test
    public void patchProfile() throws Exception {
        // given
        UserDto.PatchProfile patchProfile = newInstance(UserDto.PatchProfile.class);
        setField(patchProfile, "nickName", NICKNAME1);
        setField(patchProfile, "statusMessage", STATUS_MESSAGE1);

        String json = objectMapper.writeValueAsString(patchProfile);

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        doNothing().when(userService).editUser(ID1, NICKNAME1, STATUS_MESSAGE1);

        // when
        ResultActions actions = mockMvc.perform(
                patch("/users/profile")
                        .header(AUTHORIZATION_HEADER, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(ID1)))
                .andDo(
                        document("patch-user",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName(AUTHORIZATION_HEADER).description("Access Token")
                                ),
                                responseBody())
                );
    }

}
