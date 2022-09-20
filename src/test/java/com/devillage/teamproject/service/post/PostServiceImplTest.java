package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.util.Reflection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest implements Reflection {

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    void postBookmark() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // given
        Post post = newInstance(Post.class);
        User user = newInstance(User.class);
        setField(user, "bookmarks", new ArrayList<>());

        Long postId = 1L;
        Long userId = 1L;

        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        // when
        Bookmark findBookmark = postService.postBookmark(postId);

        // then
        Assertions.assertThat(findBookmark.getPost()).isEqualTo(post);
        Assertions.assertThat(findBookmark.getUser()).isEqualTo(user);
    }
}