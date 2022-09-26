package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.repository.comment.CommentRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.post.PostService;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.service.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.devillage.teamproject.util.TestConstants.COMMENT_CONTENT;
import static com.devillage.teamproject.util.TestConstants.ID1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

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



}