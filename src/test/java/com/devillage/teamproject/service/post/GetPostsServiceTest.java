package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.posttag.PostTagRepository;
import com.devillage.teamproject.repository.tag.TagRepository;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.devillage.teamproject.util.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetPostsServiceTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostTagRepository postTagRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void getPostsByCategory() throws Exception {
        // given
        String allCategory = "ALL";
        String existCategory = "FREE";
        String notExistCategory = "CATEGORY";
        int page = 1;
        int size = 1;
        Post post = newInstance(Post.class);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        PageImpl<Post> allPosts = new PageImpl<>(List.of(post),
                pageable, 1L);
        Page<Post> posts = new PageImpl<>(List.of(post),
                pageable, 1L);

        given(postRepository.findAll(pageable))
                .willReturn(allPosts);
        given(postRepository.findByCategory_CategoryType(CategoryType.valueOf(existCategory), pageable))
                .willReturn(posts);

        // when
        Page<Post> findAllPosts = postService.getPostsByCategory(allCategory, page, size);
        Page<Post> findPosts = postService.getPostsByCategory(existCategory, page, size);

        // then
        assertThat(findAllPosts).isEqualTo(allPosts);
        assertThat(findPosts).isEqualTo(posts);
        assertThrows(BusinessLogicException.class,
                () -> postService.getPostsByCategory(notExistCategory, page, size));
    }

    @Test
    public void getPostsByTag() throws Exception {
        // given
        String existTagName = TAGNAME1;
        String notExistTagName = TAGNAME2;
        Tag tag = newInstance(Tag.class);
        PostTag postTag1 = newInstance(PostTag.class);
        PostTag postTag2 = newInstance(PostTag.class);
        Post post1 = newInstance(Post.class);
        Post post2 = newInstance(Post.class);

        setField(postTag1, "post", post1);
        setField(postTag2, "post", post2);
        setField(postTag1, "tag", tag);
        setField(postTag2, "tag", tag);

        int page = 1;
        int size = 5;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<PostTag> postTags = new PageImpl<>(List.of(postTag1, postTag2), pageable, 2L);
        Page<Post> posts = new PageImpl<>(List.of(post1, post2), pageable, 2L);

        given(tagRepository.findTagByName(existTagName))
                .willReturn(Optional.ofNullable(tag));
        given(tagRepository.findTagByName(notExistTagName))
                .willReturn(Optional.empty());
        given(postTagRepository.findByTag(tag, pageable))
                .willReturn(postTags);

        // when
        Page<Post> postsByTag = postService.getPostsByTag(existTagName, page, size);

        // then
        assertThat(postsByTag.getNumber()).isEqualTo(posts.getNumber());
        assertThat(postsByTag.getSize()).isEqualTo(posts.getSize());
        assertThat(postsByTag.getTotalPages()).isEqualTo(posts.getTotalPages());
        assertThat(postsByTag.getTotalElements()).isEqualTo(posts.getTotalElements());
        assertThat(postsByTag.getContent()).isEqualTo(posts.getContent());
        assertThrows(BusinessLogicException.class,
                () -> postService.getPostsByTag(notExistTagName, page, size));
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
        assertThat(findPosts1.getContent().get(0)).isEqualTo(post3);
        assertThat(findPosts1.getNumber() + 1).isEqualTo(page);
        assertThat(findPosts1.getSize()).isEqualTo(size1);
        assertThat(findPosts1.getTotalPages()).isEqualTo(user.getBookmarks().size() / size1);
        assertThat(findPosts1.getTotalElements()).isEqualTo(user.getBookmarks().size());

        assertThat(findPosts2.getContent().get(0)).isEqualTo(post3);
        assertThat(findPosts2.getContent().get(1)).isEqualTo(post1);
        assertThat(findPosts2.getContent().get(2)).isEqualTo(post2);
        assertThat(findPosts2.getNumber() + 1).isEqualTo(page);
        assertThat(findPosts2.getSize()).isEqualTo(size2);
        assertThat(findPosts2.getTotalPages()).isEqualTo(user.getBookmarks().size() / size2);
        assertThat(findPosts2.getTotalElements()).isEqualTo(user.getBookmarks().size());
    }

}
