package com.devillage.teamproject.controller.comment;

import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.security.resolver.ResultJwtArgumentResolver;
import com.devillage.teamproject.service.comment.CommentService;
import com.devillage.teamproject.util.security.SecurityTestConfig;
import com.devillage.teamproject.util.security.WithMockCustomUser;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static com.devillage.teamproject.util.TestConstants.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CommentController.class, ResultJwtArgumentResolver.class})
@WithMockCustomUser
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @MockBean
    ResultJwtArgumentResolver resultJwtArgumentResolver;

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
                .andExpect(jsonPath("$.reCommentId").value(reComment.getId()))
                .andExpect(jsonPath("$.userId").value(user.getId()))
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
                                fieldWithPath("content").description("대댓글 내용"),
                                fieldWithPath("createdAt").description("대댓글 쓴 날짜"),
                                fieldWithPath("lastModifiedAt").description("대댓글을 마지막으로 수정한 날짜"),
                                fieldWithPath("isLiked").description("좋아요 여부")
                        )
                ));

    }

    @Test
    public void patchComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Post post = Post.builder().id(ID1).user(user).build();
        CommentDto.Patch patch = CommentDto.Patch.builder().content(COMMENT_CONTENT).build();
        Comment comment = Comment.builder()
                .id(ID1)
                .content(COMMENT_CONTENT)
                .user(user)
                .post(post)
                .build();

        String json = gson.toJson(patch);

        given(commentService.editComment(post.getId(), comment.getId(), patch.getContent()))
                .willReturn(comment);

        // when
        ResultActions actions = mockMvc.perform(
                patch("/posts/{post-id}/comments/{comment-id}", post.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(comment.getId()))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
                .andExpect(jsonPath("$.userId").value(comment.getUser().getId()))
                .andExpect(jsonPath("$.postId").value(comment.getPost().getId()))
                .andDo(document(
                        "comments/patch-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자"),
                                parameterWithName("comment-id").description("댓글 식별자")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글 내용")
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
    public void patchReComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Post post = Post.builder().id(ID1).user(user).build();
        CommentDto.Patch patch = CommentDto.Patch.builder().content(COMMENT_CONTENT).build();
        Comment comment = Comment.builder()
                .id(ID1)
                .content(COMMENT_CONTENT)
                .user(user)
                .post(post)
                .build();
        ReComment reComment = ReComment.builder()
                .id(ID1)
                .content(COMMENT_CONTENT)
                .user(user)
                .comment(comment)
                .build();

        String json = gson.toJson(patch);

        given(commentService.editReComment(post.getId(), comment.getId(), reComment.getId(), patch.getContent()))
                .willReturn(reComment);

        // when
        ResultActions actions = mockMvc.perform(
                patch("/posts/{post-id}/comments/{comment-id}/{re-comment-id}",
                        post.getId(), comment.getId(), reComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.reCommentId").value(reComment.getId()))
                .andExpect(jsonPath("$.userId").value(reComment.getComment().getUser().getId()))
                .andExpect(jsonPath("$.content").value(reComment.getComment().getContent()))
                .andDo(document(
                        "comments/patch-re-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자"),
                                parameterWithName("comment-id").description("댓글 식별자"),
                                parameterWithName("re-comment-id").description("대댓글 식별자")
                        ),
                        requestFields(
                                fieldWithPath("content").description("대댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("reCommentId").description("대댓글 식별자"),
                                fieldWithPath("userId").description("대댓글 작성자 식별자"),
                                fieldWithPath("content").description("대댓글 내용"),
                                fieldWithPath("createdAt").description("대댓글 작성일시"),
                                fieldWithPath("lastModifiedAt").description("대댓글 수정일시"),
                                fieldWithPath("isLiked").description("좋아요 여부")
                        )
                ));
    }

    @Test
    @DisplayName("getAllComments")
    public void getAllComments() throws Exception {
        // given
        int page = 1;
        int size = 10;
        Post post = Post.builder().id(ID1).build();
        User user = User.builder().id(ID1).build();
        Comment comment1 = Comment.builder().id(ID1).content(COMMENT_CONTENT).user(user).post(post).build();
        ReComment reComment1_1 = ReComment.builder().id(ID1).content(COMMENT_CONTENT).user(user).comment(comment1).build();
        comment1.getReComments().add(reComment1_1);
        Comment comment2 = Comment.builder().id(ID2).content(COMMENT_CONTENT).user(user).post(post).build();
        Comment comment3 = Comment.builder().id(ID2 + 1).content(COMMENT_CONTENT).user(user).post(post).build();

        given(commentService.findComments(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(new PageImpl<>(List.of(comment1, comment2, comment3)));

        // when
        ResultActions actions = mockMvc.perform(
                get("/posts/{post-id}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document(
                        "get-comments",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                fieldWithPath("data[].commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("data[].reComments").type(JsonFieldType.ARRAY).description("대댓글"),
                                fieldWithPath("data[].createdAt").description("작성시간"),
                                fieldWithPath("data[].lastModifiedAt").description("수정 시간"),
                                fieldWithPath("data[].isLiked").description("좋아요 여부"),
                                fieldWithPath("data[].reComments[].reCommentId").type(JsonFieldType.NUMBER).description("대댓글 식별자"),
                                fieldWithPath("data[].reComments[].userId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                fieldWithPath("data[].reComments[].content").type(JsonFieldType.STRING).description("대댓글 내용"),
                                fieldWithPath("data[].reComments[].createdAt").description("작성 시간"),
                                fieldWithPath("data[].reComments[].lastModifiedAt").description("수정 시간"),
                                fieldWithPath("data[].reComments[].isLiked").description("좋아요 여부"),
                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
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
//
//    @Test
//    public void likeComment() throws Exception {
//        //given
//        User user = User.builder().id(ID1).build();
//        Post post = Post.builder().id(ID1).user(user).build();
//        Comment comment = Comment.builder().id(ID1).build();
//        Long postId = post.getId();
//        Long commentId = comment.getId();
//
//        given(commentService.likeComment(eq(postId),eq(commentId),Mockito.anyString())).willReturn(comment);
//
//        //when
//        ResultActions actions = mockMvc.perform(
//                post("posts/{post-id}/comments/{comment-id}/like",postId,commentId)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//
//        );
//        //then
//        //TODO : restDocs 추가
//        actions.andExpect(status().isOk())
//                .andExpect(content().string("true"))
//                .andReturn();
//    }
//

}
