package com.devillage.teamproject.repository.post;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.util.Reflection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest implements Reflection {

    @Autowired
    private PostRepository postRepository;

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

}