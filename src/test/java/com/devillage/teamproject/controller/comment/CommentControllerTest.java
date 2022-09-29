package com.devillage.teamproject.controller.comment;

import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.service.comment.CommentService;
import org.junit.jupiter.api.Test;
import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static com.devillage.teamproject.util.TestConstants.COMMENT_CONTENT;
import static com.devillage.teamproject.util.TestConstants.ID1;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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

@WebMvcTest(controllers = CommentController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class
        })
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @Autowired
    private Gson gson;

    @Test
    void deleteReComment() throws Exception {
        // given
        Long postId = 1L;
        Long commentId = 2L;
        Long reCommentId = 3L;

        doNothing().when(commentService).deleteReComment(postId, commentId, reCommentId);

        // when
        ResultActions actions = mockMvc.perform(
                delete("/posts/{post-id}/comments/{comment-id}/{re-comment-id}",
                        postId, commentId, reCommentId)
        );

        // then
        actions.andExpect(status().isNoContent())
                .andDo(document("comments/delete-re-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자"),
                                parameterWithName("comment-id").description("댓글 식별자"),
                                parameterWithName("re-comment-id").description("대댓글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("createComment")
    public void createComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Post post = Post.builder().id(ID1).build();
        CommentDto.Post postDto = CommentDto.Post.builder().content(COMMENT_CONTENT).build();
        Comment comment = Comment.builder().content(COMMENT_CONTENT).build();
        String content = gson.toJson(postDto);

        given(commentService.createComment(Mockito.any(Comment.class), Mockito.anyString()))
                .willReturn(Comment.createComment(comment, user, post));

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, "some-token")
                        .content(content)
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
                .andDo(document(
                        "post-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("jwt 토큰")
                        ),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("commentId").description("댓글 식별자"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("userId").description("댓글 작성자 식별자"),
                                fieldWithPath("postId").description("댓글이 작성된 게시글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("createReComment")
    public void createReComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Comment comment = Comment.builder().id(ID1).build();
        CommentDto.ReCommentPost postDto = CommentDto.ReCommentPost.builder().content(COMMENT_CONTENT).build();
        ReComment reComment = ReComment.createReComment(user, comment, postDto.getContent());
        String content = gson.toJson(postDto);

        given(commentService.createReComment(Mockito.any(ReComment.class), Mockito.anyString()))
                .willReturn(reComment);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/comments/{comment-id}", ID1, comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header(AUTHORIZATION_HEADER, "some-token")
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.commentId").value(comment.getId()))
                .andExpect(jsonPath("$.content").value(postDto.getContent()))
                .andDo(document(
                        "post-recomment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("jwt 토큰")
                        ),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자"),
                                parameterWithName("comment-id").description("댓글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("reCommentId").description("대댓글 식별자"),
                                fieldWithPath("userId").description("댓글 쓴 사람 식별자"),
                                fieldWithPath("commentId").description("댓글 식별자"),
                                fieldWithPath("content").description("대댓글 내용"),
                                fieldWithPath("createdAt").description("대댓글 쓴 날짜"),
                                fieldWithPath("lastModifiedAt").description("대댓글을 마지막으로 수정한 날짜")
                        )
                ));

    }

    @Test
    @DisplayName("deleteComment")
    public void deleteComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Comment comment = Comment.builder().id(ID1).build();

        // when
        ResultActions actions = mockMvc.perform(
                delete("/posts/{post-id}/comments/{comment-id}", ID1, comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, "some-token")
        );

        // then
        actions.andExpect(status().isNoContent())
                .andDo(document(
                        "delete-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("jwt 토큰")
                        ),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자"),
                                parameterWithName("comment-id").description("댓글 식별자")
                        )
                ));
    }
}