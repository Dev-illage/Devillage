package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.util.Reflection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetPostsTest implements Reflection {

    @Mock
    private PostRepository postRepository;

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

}
