package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReportedPost;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.post.ReportedPostRepository;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReportTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReportedPostRepository reportRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private PostServiceImpl postService;

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);
    Long userId = 1L; // Security 메서드 구현 필요
    Long postId = 1L;

    ReportTest() throws Exception {
        setField(user, "reportedPosts", new ArrayList<>());
    }

    @Test
    void createReportedPost() {
        // given
        given(jwtTokenUtil.getUserId(anyString()))
                .willReturn(1L);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        given(reportRepository.findByUserIdAndPostId(userId, postId))
                .willReturn(new ArrayList<>());

        // when
        ReportedPost findReportedPost = postService.postReport("", postId);

        // then
        Assertions.assertThat(findReportedPost.getPost()).isEqualTo(post);
        Assertions.assertThat(findReportedPost.getUser()).isEqualTo(user);
    }

    @Test
    void alreadyReportedPost() {
        // given
        ReportedPost report = new ReportedPost(user, post);

        given(jwtTokenUtil.getUserId(anyString()))
                .willReturn(1L);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        given(reportRepository.findByUserIdAndPostId(userId, postId))
                .willReturn(List.of(report));

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postReport("", postId));
    }

}