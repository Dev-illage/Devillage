package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.CommentStatus;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.comment.CommentRepository;
import com.devillage.teamproject.repository.comment.ReCommentRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.post.PostService;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Optional;

import static com.devillage.teamproject.util.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest implements Reflection {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReCommentRepository reCommentRepository;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private PostService postService;
    @Mock
    private UserService userService;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void deleteReComment() throws Exception {
        // given
        Post post = newInstance(Post.class);
        Comment comment = newInstance(Comment.class);
        ReComment reComment = newInstance(ReComment.class);

        setField(post, "id", 1L);
        setField(comment, "id", 2L);
        setField(reComment, "id", 3L);

        setField(reComment, "comment", comment);
        setField(comment, "post", post);

        doNothing().when(reCommentRepository).deleteById(anyLong());
        given(reCommentRepository.findById(reComment.getId()))
                .willReturn(Optional.of(reComment));
        given(reCommentRepository.findById(reComment.getId() + 1L))
                .willReturn(Optional.empty());

        // when / then
        assertDoesNotThrow(
                () -> commentService.deleteReComment(post.getId(), comment.getId(), reComment.getId()));
        assertThrows(BusinessLogicException.class,
                () -> commentService.deleteReComment(post.getId() + 1L, comment.getId(), reComment.getId()));
        assertThrows(BusinessLogicException.class,
                () -> commentService.deleteReComment(post.getId(), comment.getId() + 1L, reComment.getId()));
        assertThrows(BusinessLogicException.class,
                () -> commentService.deleteReComment(post.getId(), comment.getId(), reComment.getId() + 1L));
    }

    @Test
    @DisplayName("createComment")
    public void createComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Post post = Post.builder().id(ID1).build();
        Comment comment = Comment.builder().content(COMMENT_CONTENT).post(post).build();

        given(userService.findVerifiedUser(Mockito.anyLong())).willReturn(user);
        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(user.getId());
        given(postService.getPost(Mockito.anyLong())).willReturn(post);
        Comment expectedComment = Comment.createComment(comment, user, post);
        given(commentRepository.save(Mockito.any(Comment.class))).willReturn(expectedComment);

        // when
        Comment actualComment = commentService.createComment(comment, "someToken");

        // then
        assertEquals(user, actualComment.getUser());
        assertEquals(post, actualComment.getPost());
        assertEquals(actualComment, user.getComments().get(0));
        assertEquals(actualComment, post.getComments().get(0));
    }

    @Test
    public void editComment() throws Exception {
        // given
        Post post = Post.builder().id(ID1).build();
        Comment comment = Comment.builder().id(ID1).content(COMMENT_CONTENT).post(post).build();
        String newContent = COMMENT_CONTENT + "2";

        given(commentRepository.findById(comment.getId()))
                .willReturn(Optional.of(comment));
        given(commentRepository.findById(ID2))
                .willReturn(Optional.empty());

        // when
        Comment returnedComment = commentService.editComment(post.getId(), comment.getId(), newContent);

        // then
        assertThat(returnedComment).isEqualTo(comment);
        assertThrows(BusinessLogicException.class,
                () -> commentService.editComment(post.getId(), ID2, newContent));
        assertThrows(BusinessLogicException.class,
                () -> commentService.editComment(ID2, comment.getId(), newContent));
    }

    @Test
    public void editReComment() throws Exception {
        // given
        Post post = Post.builder().id(ID1).build();
        Comment comment = Comment.builder().id(ID1).content(COMMENT_CONTENT).post(post).build();
        ReComment reComment = ReComment.builder().id(ID1).content(COMMENT_CONTENT).comment(comment).build();
        String newContent = COMMENT_CONTENT + "2";

        given(reCommentRepository.findById(comment.getId()))
                .willReturn(Optional.of(reComment));
        given(reCommentRepository.findById(ID2))
                .willReturn(Optional.empty());

        // when
        ReComment returnedReComment =
                commentService.editReComment(post.getId(), comment.getId(), reComment.getId(), newContent);

        // then
        assertThat(returnedReComment).isEqualTo(reComment);
        assertThrows(BusinessLogicException.class,
                () -> commentService.editReComment(post.getId(), comment.getId(), ID2, newContent));
        assertThrows(BusinessLogicException.class,
                () -> commentService.editReComment(post.getId(), ID2, reComment.getId(), newContent));
        assertThrows(BusinessLogicException.class,
                () -> commentService.editReComment(ID2, comment.getId(), reComment.getId(), newContent));
    }

    @Test
    @DisplayName("createReComment")
    public void createReComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Comment comment = Comment.builder().id(ID1).build();
        ReComment reCommentDto = ReComment.builder().content(COMMENT_CONTENT)
                .comment(comment).build();

        given(commentRepository.findById(Mockito.anyLong())).willReturn(Optional.of(comment));
        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(user.getId());
        given(userService.findVerifiedUser(user.getId())).willReturn(user);
        given(reCommentRepository.save(Mockito.any(ReComment.class))).willAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        ReComment actualReComment = commentService.createReComment(reCommentDto, "someToken");

        // then
        assertEquals(reCommentDto.getContent(), actualReComment.getContent());
        assertEquals(user, actualReComment.getUser());
        assertEquals(comment, actualReComment.getComment());
    }

    /**
     * 일반 삭제는 단위테스트로 검증하기엔 불가능해 보입니다.
     */
    @Test
    @DisplayName("대댓글이 있는 상태에서의 삭제")
    public void deleteCommentWithReComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Comment comment = Comment.builder().id(ID1).content(COMMENT_CONTENT).user(user).build();
        ReComment reComment = ReComment.builder().id(ID1).comment(comment).build();
        comment.getReComments().add(reComment);

        given(commentRepository.findById(Mockito.anyLong())).willReturn(Optional.of(comment));
        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(user.getId());

        // when
        commentService.deleteComment(comment.getId(), "someToken");

        // then
        assertEquals(CommentStatus.DELETED, comment.getCommentStatus());
    }

    @Test
    @DisplayName("유저가 일치하지 않으면 삭제가 불가능")
    public void deleteComment() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        Comment comment = Comment.builder().id(ID1).content(COMMENT_CONTENT).user(user).build();
        ReComment reComment = ReComment.builder().id(ID1).comment(comment).build();
        comment.getReComments().add(reComment);

        given(commentRepository.findById(Mockito.anyLong())).willReturn(Optional.of(comment));
        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(user.getId() + 1);

        // when then
        assertThrows(BusinessLogicException.class,
                () -> commentService.deleteComment(comment.getId(), "someToken"));
    }

    @Test
    @DisplayName("getAllComments")
    public void getAllComments() throws Exception {
        // given
        Comment comment1 = Comment.builder().id(ID1).content(COMMENT_CONTENT).build();
        ReComment reComment1_1 = ReComment.builder().id(ID1).content(COMMENT_CONTENT).comment(comment1).build();
        comment1.getReComments().add(reComment1_1);
        Comment comment2 = Comment.builder().id(ID2).content(COMMENT_CONTENT).build();
        Comment comment3 = Comment.builder().id(ID2 + 1).content(COMMENT_CONTENT).build();

        given(postService.getPost(Mockito.anyLong())).willReturn(null);
        given(commentRepository.findAllByPostId(Mockito.anyLong(), Mockito.any()))
                .willReturn(new PageImpl<>(List.of(comment1, comment2, comment3)));

        // when
        Page<Comment> commentPage = commentService.findComments(ID1, 1, 10);

        // then
        assertEquals(3, commentPage.getTotalElements());

    }

}