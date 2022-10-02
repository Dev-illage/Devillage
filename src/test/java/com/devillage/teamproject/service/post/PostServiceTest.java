package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest implements Reflection {

    @Mock
    private PostRepository postRepository;

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
    @DisplayName("수정이 필요한 테스트 입니다.(빌드 오류)")
    public void savePost() throws Exception {
        //given
        setField(post, "title", "Mockito 관련 질문입니다.");
        setField(post, "content", "안녕하세요. 스트링 통째로 드가는게 맞나요");
        setField(post, "tags", new ArrayList<>());

        given(postRepository.save(Mockito.any(Post.class))).willReturn(post);

//        //when
//        Post savedPost = postService.savePost(post);
//
//        //then
//        assertThat(post.getTitle()).isEqualTo(savedPost.getTitle());
    }

    @Test
    public void userNotFound() {
        // given
        given(userService.findVerifiedUser(userId))
                .willReturn(null);

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postBookmark(userId, postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postReport(userId, postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postLike(userId, postId));
    }

    @Test
    public void postNotFound() {
        // given
        given(postRepository.findById(postId))
                .willReturn(Optional.empty());

        given(userService.findVerifiedUser(userId))
                .willReturn(user);

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postBookmark(userId, postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postReport(userId, postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postLike(userId, postId));
    }
}
