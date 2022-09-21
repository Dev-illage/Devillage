package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.BookmarkRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookmarkTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private PostServiceImpl postService;

    Post post = newInstance(Post.class);
    User user = newInstance(User.class);
    Long postId = 1L;
    Long userId = 1L; // Security 메서드 구현 필요

    BookmarkTest() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        setField(user, "bookmarks", new ArrayList<>());
    };

    @Test
    void createBookmark() {
        // given
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));


        given(bookmarkRepository.findByPostIdAndUserId(postId, userId))
                .willReturn(new ArrayList<>());

        // when
        Bookmark findBookmark = postService.postBookmark(postId);

        // then
        Assertions.assertThat(findBookmark.getPost()).isEqualTo(post);
        Assertions.assertThat(findBookmark.getUser()).isEqualTo(user);
    }

    @Test
    void deleteBookmark() {
        // given
        Bookmark bookmark = new Bookmark(user, post);

        given(bookmarkRepository.findByPostIdAndUserId(postId, userId))
                .willReturn(List.of(bookmark));

        // when
        Bookmark findBookmark = postService.postBookmark(postId);

        // then
        Assertions.assertThat(findBookmark).isEqualTo(bookmark);
    }

    @Test
    public void postNotFound() {
        // given
        given(postRepository.findById(postId))
                .willReturn(Optional.empty());

        // when / then
        assertThrows(BusinessLogicException.class,
                () -> postService.postBookmark(postId));
    }
}