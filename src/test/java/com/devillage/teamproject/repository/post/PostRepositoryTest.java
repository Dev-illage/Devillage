package com.devillage.teamproject.repository.post;

import com.devillage.teamproject.entity.Category;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.repository.category.CategoryRepository;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest implements Reflection {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void saveAndFindById() throws Exception {
        // given
        Post post = newInstance(Post.class);

        postRepository.save(post);

        // when
        Post findPost = postRepository.findById(post.getId()).get();

        // then
        assertThat(findPost).isEqualTo(post);
    }

    @Test
    public void findByCategory_CategoryType() throws Exception {
        // given
        Post post1 = newInstance(Post.class);
        Post post2 = newInstance(Post.class);
        Post post3 = newInstance(Post.class);
        Category category1 = newInstance(Category.class);
        Category category2 = newInstance(Category.class);
        CategoryType free = CategoryType.FREE;
        CategoryType notice = CategoryType.NOTICE;
        int page = 1;
        int size = 3;

        setField(category1, "categoryType", free);
        setField(category2, "categoryType", notice);
        setField(post1, "category", category1);
        setField(post2, "category", category1);
        setField(post3, "category", category2);

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // when
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        Page<Post> frees = postRepository.findByCategory_CategoryType(
                free, pageable);
        Page<Post> notices = postRepository.findByCategory_CategoryType(
                notice, pageable);

        // then
        assertThat(frees.getContent().size()).isEqualTo(2);
        assertThat(frees.getContent().get(0)).isEqualTo(post2);
        assertThat(frees.getContent().get(1)).isEqualTo(post1);

        assertThat(notices.getContent().size()).isEqualTo(1);
        assertThat(notices.getContent().get(0)).isEqualTo(post3);
    }

    @Test
    public void findBySearch() throws Exception {
        // given
        int page = 1;
        int size = 3;

        Post post1 = newInstance(Post.class);
        Post post2 = newInstance(Post.class);
        Post post3 = newInstance(Post.class);
        Post post4 = newInstance(Post.class);

        setField(post1, "title", "검색어1");
        setField(post2, "content", "검색어1");
        setField(post3, "title", "검색어2");
        setField(post4, "content", "검색어2");

        postRepository.saveAll(List.of(post1, post2, post3, post4));

        String word1 = "검색어1";
        String word2 = "검색어2";
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        // when
        Page<Post> posts1 = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(word1, word1, pageable);
        Page<Post> posts2 = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(word2, word2, pageable);

        // then
        assertThat(posts1.getContent().size()).isEqualTo(2);
        assertThat(posts1.getNumber()).isEqualTo(page - 1);
        assertThat(posts1.getSize()).isEqualTo(size);
        assertThat(posts1.getTotalPages()).isEqualTo(2 / size + 1);
        assertThat(posts1.getTotalElements()).isEqualTo(2);
        assertThat(posts1.getContent().get(0)).isIn(List.of(post1, post2));
        assertThat(posts1.getContent().get(1)).isIn(List.of(post1, post2));

        assertThat(posts2.getContent().size()).isEqualTo(2);
        assertThat(posts2.getNumber()).isEqualTo(page - 1);
        assertThat(posts2.getSize()).isEqualTo(size);
        assertThat(posts2.getTotalPages()).isEqualTo(2 / size + 1);
        assertThat(posts2.getTotalElements()).isEqualTo(2);
        assertThat(posts2.getContent().get(0)).isIn(List.of(post3, post4));
        assertThat(posts2.getContent().get(1)).isIn(List.of(post3, post4));
    }

}