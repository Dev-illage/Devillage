package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.repository.post.BookmarkRepository;
import com.devillage.teamproject.repository.post.PostRepository;
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

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookmarkTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceImpl postService;

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);
    Long userId = 1L;
    Long postId = 1L;

    BookmarkTest() throws Exception {
        setField(user, "bookmarks", new ArrayList<>());
    }

    @Test
    void createBookmark() {
        // given
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));
        given(userService.findVerifiedUser(userId))
                .willReturn(user);

        given(bookmarkRepository.findByUserIdAndPostId(userId, postId))
                .willReturn(new ArrayList<>());

        // when
        Bookmark findBookmark = postService.postBookmark(userId, postId);

        // then
        Assertions.assertThat(findBookmark.getPost()).isEqualTo(post);
        Assertions.assertThat(findBookmark.getUser()).isEqualTo(user);
    }

    @Test
    void deleteBookmark() {
        // given
        Bookmark bookmark = new Bookmark(user, post);

        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));
        given(userService.findVerifiedUser(userId))
                .willReturn(user);

        given(bookmarkRepository.findByUserIdAndPostId(userId, postId))
                .willReturn(List.of(bookmark));

        // when
        Bookmark findBookmark = postService.postBookmark(userId, postId);

        // then
        Assertions.assertThat(findBookmark).isEqualTo(bookmark);
    }

}