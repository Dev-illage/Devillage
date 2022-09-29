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
import com.devillage.teamproject.service.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devillage.teamproject.util.TestConstants.COMMENT_CONTENT;
import static com.devillage.teamproject.util.TestConstants.ID1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private PostService postService;
    @Mock
    private UserService userService;
    @Mock
    private ReCommentRepository reCommentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

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

}