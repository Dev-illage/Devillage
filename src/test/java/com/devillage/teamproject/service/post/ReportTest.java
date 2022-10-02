package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReportedPost;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.post.ReportedPostRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.user.UserService;
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
    private ReportedPostRepository reportRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceImpl postService;

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);
    Long userId = 1L;
    Long postId = 1L;

    ReportTest() throws Exception {
        setField(user, "reportedPosts", new ArrayList<>());
    }

    @Test
    void createReportedPost() {
        // given
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        given(reportRepository.findByUserIdAndPostId(userId, postId))
                .willReturn(new ArrayList<>());

        given(userService.findVerifiedUser(userId))
                .willReturn(user);

        // when
        ReportedPost findReportedPost = postService.postReport(userId, postId);

        // then
        Assertions.assertThat(findReportedPost.getPost()).isEqualTo(post);
        Assertions.assertThat(findReportedPost.getUser()).isEqualTo(user);
    }

    @Test
    void alreadyReportedPost() {
        // given
        ReportedPost report = new ReportedPost(user, post);

        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        given(reportRepository.findByUserIdAndPostId(userId, postId))
                .willReturn(List.of(report));

        given(userService.findVerifiedUser(userId))
                .willReturn(user);

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postReport(userId, postId));
    }

}