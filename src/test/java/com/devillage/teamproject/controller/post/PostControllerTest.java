package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.service.post.PostService;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.InvocationTargetException;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class
        })
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class PostControllerTest implements Reflection {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);

    PostControllerTest() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        setField(user, "id", 1L);
        setField(post, "id", 2L);
    }

    @Test
    void postBookmark() throws Exception {
        // given
        Bookmark bookmark = new Bookmark(user, post);
        setField(bookmark, "id", 3L);

        given(postService.postBookmark(post.getId()))
                .willReturn(bookmark);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/bookmark", post.getId())
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user").value(user.getId()))
                .andExpect(jsonPath("$.data.post").value(post.getId()))
                .andExpect(jsonPath("$.data.bookmark").value(bookmark.getId()))
                .andDo(document("post-bookmark",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                fieldWithPath("data.user").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("data.post").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("data.bookmark").type(JsonFieldType.NUMBER).description("북마크 식별자")
                        )
                ));

    }

    @Test
    void postReport() throws Exception {
        // given
        ReportedPost report = new ReportedPost(user, post);
        setField(report, "id", 3L);

        given(postService.postReport(post.getId()))
                .willReturn(report);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/report", post.getId())
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user").value(user.getId()))
                .andExpect(jsonPath("$.data.post").value(post.getId()))
                .andExpect(jsonPath("$.data.report").value(report.getId()))
                .andDo(document("post-report",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                fieldWithPath("data.user").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("data.post").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("data.report").type(JsonFieldType.NUMBER).description("신고글 식별자")
                        )
                ));

    }

    @Test
    void postLike() throws Exception {
        // given
        Like like = new Like(user, post);
        setField(like, "id", 3L);

        given(postService.postLike(post.getId()))
                .willReturn(like);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/like", post.getId())
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user").value(user.getId()))
                .andExpect(jsonPath("$.data.post").value(post.getId()))
                .andExpect(jsonPath("$.data.like").value(like.getId()))
                .andDo(document("post-like",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                fieldWithPath("data.user").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("data.post").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("data.like").type(JsonFieldType.NUMBER).description("좋아요 식별자")
                        )
                ));

    }

}