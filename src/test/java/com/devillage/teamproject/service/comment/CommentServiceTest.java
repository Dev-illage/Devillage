package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.entity.User;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

}