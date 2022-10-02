package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.PostRepository;
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
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetPostsServiceTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void getPostsByCategory() throws Exception {
        // given
        String existCategory = "FREE";
        String notExistCategory = "CATEGORY";
        int page = 1;
        int size = 1;
        Post post = newInstance(Post.class);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<Post> posts = new PageImpl<>(List.of(post),
                pageable, 1L);

        given(postRepository.findByCategory_CategoryType(CategoryType.valueOf(existCategory), pageable))
                .willReturn(posts);

        // when
        Page<Post> findPosts = postService.getPostsByCategory(existCategory, page, size);

        // then
        Assertions.assertThat(findPosts).isEqualTo(posts);
        assertThrows(BusinessLogicException.class,
                () -> postService.getPostsByCategory(notExistCategory, page, size));
    }

    @Test
    public void getPostsByBookmark() throws Exception {
        // given
        Post post1 = newInstance(Post.class);
        Post post2 = newInstance(Post.class);
        Post post3 = newInstance(Post.class);
        Bookmark bookmark1 = newInstance(Bookmark.class);
        Bookmark bookmark2 = newInstance(Bookmark.class);
        Bookmark bookmark3 = newInstance(Bookmark.class);
        User user = newInstance(User.class);

        setField(post1, "id", 1L);
        setField(post2, "id", 2L);
        setField(post3, "id", 3L);
        setField(bookmark1, "post", post1);
        setField(bookmark2, "post", post2);
        setField(bookmark3, "post", post3);
        setField(bookmark1, "id", 2L);
        setField(bookmark2, "id", 1L);
        setField(bookmark3, "id", 3L);
        setField(bookmark1, "user", user);
        setField(bookmark2, "user", user);
        setField(bookmark3, "user", user);
        setField(user, "id", 1L);
        setField(user, "bookmarks", List.of(bookmark1, bookmark2, bookmark3));

        int page = 1;
        int size1 = 1;
        int size2 = 3;

        given(userService.findVerifiedUser(user.getId()))
                .willReturn(user);

        // when
        Page<Post> findPosts1 = postService.getPostsByBookmark(user.getId(), page, size1);
        Page<Post> findPosts2 = postService.getPostsByBookmark(user.getId(), page, size2);

        // then
        Assertions.assertThat(findPosts1.getContent().get(0)).isEqualTo(post3);
        Assertions.assertThat(findPosts1.getNumber() + 1).isEqualTo(page);
        Assertions.assertThat(findPosts1.getSize()).isEqualTo(size1);
        Assertions.assertThat(findPosts1.getTotalPages()).isEqualTo(user.getBookmarks().size() / size1);
        Assertions.assertThat(findPosts1.getTotalElements()).isEqualTo(user.getBookmarks().size());

        Assertions.assertThat(findPosts2.getContent().get(0)).isEqualTo(post3);
        Assertions.assertThat(findPosts2.getContent().get(1)).isEqualTo(post1);
        Assertions.assertThat(findPosts2.getContent().get(2)).isEqualTo(post2);
        Assertions.assertThat(findPosts2.getNumber() + 1).isEqualTo(page);
        Assertions.assertThat(findPosts2.getSize()).isEqualTo(size2);
        Assertions.assertThat(findPosts2.getTotalPages()).isEqualTo(user.getBookmarks().size() / size2);
        Assertions.assertThat(findPosts2.getTotalElements()).isEqualTo(user.getBookmarks().size());
    }

}
