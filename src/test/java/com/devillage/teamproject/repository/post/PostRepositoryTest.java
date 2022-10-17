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
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        long prevAllSize = postRepository.findAll(pageable)
                .getTotalElements();
        long prevFreesSize = postRepository.findDistinctByCategory_CategoryType(free, pageable)
                .getTotalElements();
        long prevNoticesSize = postRepository.findDistinctByCategory_CategoryType(notice, pageable)
                .getTotalElements();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // when
        Page<Post> allPosts = postRepository.findAll(pageable);
        Page<Post> frees = postRepository.findDistinctByCategory_CategoryType(free, pageable);
        Page<Post> notices = postRepository.findDistinctByCategory_CategoryType(notice, pageable);

        // then
        assertThat(allPosts.getTotalElements() - prevAllSize).isEqualTo(3);
        assertThat(frees.getTotalElements() - prevFreesSize).isEqualTo(2);
        assertThat(notices.getTotalElements() - prevNoticesSize).isEqualTo(1);
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

        String word1 = "검색어1";
        String word2 = "검색어2";
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        long prevWord1Size = postRepository.findDistinctByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(word1, word1, pageable)
                .getTotalElements();
        long prevWord2Size = postRepository.findDistinctByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(word2, word2, pageable)
                .getTotalElements();

        setField(post1, "title", word1);
        setField(post2, "content", word1);
        setField(post3, "title", word2);
        setField(post4, "content", word2);

        postRepository.saveAll(List.of(post1, post2, post3, post4));


        // when
        Page<Post> posts1 = postRepository.findDistinctByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(word1, word1, pageable);
        Page<Post> posts2 = postRepository.findDistinctByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(word2, word2, pageable);

        // then
        assertThat(posts1.getTotalElements() - prevWord1Size).isEqualTo(2);
        assertThat(posts2.getTotalElements() - prevWord2Size).isEqualTo(2);
    }

}