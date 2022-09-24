package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Like;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.repository.post.LikeRepository;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.util.Reflection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LikeTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private PostServiceImpl postService;

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);
    Long userId = 1L; // Security 메서드 구현 필요
    Long postId = 1L;

    LikeTest() throws Exception {
        setField(user, "likes", new ArrayList<>());
    }

    @Test
    void createLike() {
        // given
        given(jwtTokenUtil.getUserId(anyString()))
                .willReturn(1L);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        given(likeRepository.findByUserIdAndPostId(userId, postId))
                .willReturn(new ArrayList<>());

        // when
        Like findLike = postService.postLike("", postId);

        // then
        Assertions.assertThat(findLike.getPost()).isEqualTo(post);
        Assertions.assertThat(findLike.getUser()).isEqualTo(user);
    }

    @Test
    void deleteLike() {
        // given
        Like like = new Like(user, post);

        given(jwtTokenUtil.getUserId(anyString()))
                .willReturn(1L);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        given(likeRepository.findByUserIdAndPostId(userId, postId))
                .willReturn(List.of(like));

        // when
        Like findLike = postService.postLike("", postId);

        // then
        Assertions.assertThat(findLike).isEqualTo(like);
    }

}