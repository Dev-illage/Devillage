package com.devillage.teamproject.service.post;

import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.category.CategoryRepository;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.posttag.PostTagRepository;
import com.devillage.teamproject.repository.tag.TagRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devillage.teamproject.util.TestConstants.COMMENT_CONTENT;
import static com.devillage.teamproject.util.TestConstants.ID1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest implements Reflection {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostTagRepository postTagRepository;

    @Mock
    private TagRepository tagRepository;

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
    public void savePost() throws Exception {
        //given
        Category category = newInstance(Category.class);
        PostTag postTag = newInstance(PostTag.class);
        Tag tag = newInstance(Tag.class);

        setField(user, "id", 1L);
        setField(user,"nickName","dodan");
        setField(post, "id", 1L);
        setField(post, "category", category);
        setField(post, "tags", new ArrayList<>());
        setField(post, "title", "titleTest");
        setField(category, "id", 1L);
        setField(category, "categoryType", CategoryType.NOTICE);
        setField(postTag,"id",1L);
        setField(postTag, "tag", tag);
        setField(tag, "id", 1L);
        setField(tag, "name", "mvcTest");

        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(user));
        given(categoryRepository.findCategoriesByCategoryType(Mockito.any(CategoryType.class))).willReturn(category);
        given(postTagRepository.save(Mockito.any(PostTag.class))).willReturn(postTag);
        given(tagRepository.save(Mockito.any(Tag.class))).willReturn(tag);

        //when
        Post savedPost = postService.savePost(post,post.getCategory().getCategoryType(),List.of(tag.getName()),user.getId());

        //then
        assertThat(post.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(post.getCategory()).isEqualTo(savedPost.getCategory());
        assertThat(post.getContent()).isEqualTo(savedPost.getContent());
    }

    @Test
    public void editPost() throws Exception {
        //given
        Category category = newInstance(Category.class);
        PostTag postTag = newInstance(PostTag.class);
        Tag tag = newInstance(Tag.class);

        setField(user, "id", 1L);
        setField(user,"nickName","dodan");
        setField(post, "id", 1L);
        setField(post, "category", category);
        setField(post, "tags", new ArrayList<>());
        setField(post, "title", "titleTest");
        setField(category, "id", 1L);
        setField(category, "categoryType", CategoryType.NOTICE);
        setField(postTag,"id",1L);
        setField(postTag, "tag", tag);
        setField(tag, "id", 1L);
        setField(tag, "name", "mvcTest");


        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(user));
        given(categoryRepository.findCategoriesByCategoryType(Mockito.any(CategoryType.class))).willReturn(category);
        given(postTagRepository.save(Mockito.any(PostTag.class))).willReturn(postTag);
        given(tagRepository.save(Mockito.any(Tag.class))).willReturn(tag);

        //when
        Post editedPost = postService.editPost(post,post.getCategory().getCategoryType(),List.of(tag.getName()),user.getId(),postId);

        //then
        assertThat(post.getTitle()).isEqualTo(editedPost.getTitle());
        assertThat(post.getCategory()).isEqualTo(editedPost.getCategory());
        assertThat(post.getContent()).isEqualTo(editedPost.getContent());
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
                () -> postService.postReport(userId, postId, 1, ""));
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
                () -> postService.postReport(userId, postId, 1, ""));
        assertThrows(BusinessLogicException.class,
                () -> postService.postLike(userId, postId));
    }

    @Test
    public void getPost() throws Exception{
        //given
        UserDto.AuthorInfo authorInfo = newInstance(UserDto.AuthorInfo.class);
        Category category = newInstance(Category.class);
        PostTag postTag = newInstance(PostTag.class);
        Tag tag = newInstance(Tag.class);
        Comment comment = newInstance(Comment.class);

        setField(user, "id", 1L);
        setField(post, "id", 1L);
        setField(post, "category", category);
        setField(post, "title", "Mockito 관련 질문입니다.");
        setField(post, "tags", List.of(postTag));
        setField(post, "content", "안녕하세요. 스트링 통째로 드가는게 맞나요");
        setField(post, "clicks", 1L);
        setField(category, "categoryType", CategoryType.NOTICE);
        setField(postTag, "tag", tag);
        setField(tag, "id", 1L);
        setField(tag, "name", "mvcTest");
        setField(comment, "id", 1L);
        setField(comment, "content", "잘 봤습니다.");
        setField(authorInfo, "authorId", 1L);
        setField(authorInfo, "authorName", "강지");
        post.setDate();

        given(postRepository.findById(post.getId())).willReturn(Optional.ofNullable(post));

        //when
        Post testPost = postService.getPost(user.getId());

        //then
        assertEquals(testPost.getContent(),post.getContent());
        assertEquals(testPost.getClicks(),post.getClicks());
        assertEquals(testPost.getCategory(),post.getCategory());
        assertEquals(testPost.getTags().get(0).getTag().getName(),post.getTags().get(0).getTag().getName());

    }

    @Test
    public void deletePost() throws Exception{
        //given
        Post post = newInstance(Post.class);
        setField(post,"id",1L);

        Long postId = 1L;
        doNothing().when(postRepository).deleteById(postId);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        //when then
        assertDoesNotThrow(() -> postService.deletePost(postId));

    }

}
