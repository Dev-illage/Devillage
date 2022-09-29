package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceImpl postService;

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);
    Long postId = 1L;
    Long userId = 1L;

    public PostServiceTest() throws Exception {
    }

    @Test
    public void savePost() throws Exception {
        //given
        setField(post, "title", "Mockito 관련 질문입니다.");
        setField(post, "content", "안녕하세요. 스트링 통째로 드가는게 맞나요");
        setField(post, "tags", new ArrayList<>());

        given(postRepository.save(Mockito.any(Post.class))).willReturn(post);

        //when
        Post savedPost = postService.savePost(post);

        //then
        assertThat(post.getTitle()).isEqualTo(savedPost.getTitle());
    }

    @Test
    public void deletePost() throws Exception{
        //given
        Post post = newInstance(Post.class);
        setField(post,"id",1L);

        Long postId = 1L;
        doNothing().when(postRepository).deleteById(postId);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        //when then
        assertDoesNotThrow(() -> postService.deletePost(postId));

    }


    @Test
    public void userNotFound() {
        // given
        given(jwtTokenUtil.getUserId(anyString()))
                .willReturn(1L);

        given(userService.findVerifiedUser(userId))
                .willReturn(null);

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postBookmark("", postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postReport("", postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postLike("", postId));
    }

    @Test
    public void postNotFound() {
        // given
        given(jwtTokenUtil.getUserId(anyString()))
                .willReturn(1L);

        given(postRepository.findById(postId))
                .willReturn(Optional.empty());

        given(userService.findVerifiedUser(userId))
                .willReturn(user);

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postBookmark("", postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postReport("", postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postLike("", postId));
    }
}