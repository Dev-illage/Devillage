package com.devillage.teamproject.repository.post;

import com.devillage.teamproject.entity.Category;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.repository.category.CategoryRepository;
import com.devillage.teamproject.util.Reflection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
        Assertions.assertThat(findPost).isEqualTo(post);
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
        Page<Post> frees = postRepository.findByCategory_CategoryType(
                free, PageRequest.of(page - 1, size, Sort.by("id").descending()));
        Page<Post> notices = postRepository.findByCategory_CategoryType(
                notice, PageRequest.of(page - 1, size, Sort.by("id").descending()));

        // then
        Assertions.assertThat(frees.getContent().size()).isEqualTo(2);
        Assertions.assertThat(frees.getContent().get(0)).isEqualTo(post2);
        Assertions.assertThat(frees.getContent().get(1)).isEqualTo(post1);

        Assertions.assertThat(notices.getContent().size()).isEqualTo(1);
        Assertions.assertThat(notices.getContent().get(0)).isEqualTo(post3);
    }

}