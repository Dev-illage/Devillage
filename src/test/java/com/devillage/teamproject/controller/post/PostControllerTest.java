package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

    @Test
    void postBookmark() throws Exception {
        // given
        Long postId = 1L;

        User user = newInstance(User.class);
        Post post = newInstance(Post.class);
        Bookmark bookmark = new Bookmark(user, post);
        setField(bookmark, "id", 1L);

        given(postService.postBookmark(postId))
                .willReturn(bookmark);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/bookmark", postId)
        );

        // then
        actions.andExpect(status().isOk())
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
}