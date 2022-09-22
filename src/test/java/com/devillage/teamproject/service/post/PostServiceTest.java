package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostServiceImpl postService;

    User user = newInstance(User.class);
    Long postId = 1L;
    Long userId = 1L;

    public PostServiceTest() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    }

    @Test
    public void userNotFound() {
        // given
        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postBookmark(postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postReport(postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postLike(postId));
    }

    @Test
    public void postNotFound() {
        // given
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(postRepository.findById(postId))
                .willReturn(Optional.empty());

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postBookmark(postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postReport(postId));
        assertThrows(BusinessLogicException.class,
                () -> postService.postLike(postId));
    }
}
