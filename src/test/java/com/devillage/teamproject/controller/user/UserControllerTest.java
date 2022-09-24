package com.devillage.teamproject.controller.user;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.devillage.teamproject.security.util.JwtConstants.*;
import static com.devillage.teamproject.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@Slf4j
class UserControllerTest implements Reflection {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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

        given(userService.findUser(Mockito.anyLong())).willReturn(user);

        // when
        ResultActions actions = mockMvc.perform(
                get("/users/profile/{user-id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.nickname").value(user.getNickName()))
                .andExpect(jsonPath("$.data.statusMessage").value(user.getStatusMessage()))
                .andDo(document(
                        "get-user-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("user-id").description("회원 식별자")
                        ),
                        responseFields(
                                fieldWithPath("data").description("결과 데이터"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.nickname").description("닉네임"),
                                fieldWithPath("data.statusMessage").description("상태메시지"),
                                fieldWithPath("data.passwordModifiedAt").description("최근 암호 수정날짜")
                        )
                ))
                .andReturn();

        String actualPasswordModifiedAt = JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.data.passwordModifiedAt").toString();

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
                delete("/users/{user-id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isNoContent())
                .andDo(document(
                        "delete-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("user-id").description("회원 식별자")
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

        given(userService.blockUser(Mockito.anyLong(), Mockito.anyLong())).willReturn(block);

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
}
