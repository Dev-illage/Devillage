package com.devillage.teamproject.repository.post;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.PostTag;
import com.devillage.teamproject.entity.Tag;
import com.devillage.teamproject.repository.posttag.PostTagRepository;
import com.devillage.teamproject.repository.tag.TagRepository;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.devillage.teamproject.util.TestConstants.TAGNAME1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TagRepositoryTest implements Reflection {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void findTag() throws Exception {
        // given
        String notExistTagName = "cxasdcqwefsadvfghrte";
        String existTagName = TAGNAME1;
        Tag tag = newInstance(Tag.class);
        setField(tag, "name", existTagName);

        tagRepository.save(tag);

        // when
        Optional<Tag> notExistTag = tagRepository.findTagByName(notExistTagName);
        Optional<Tag> existTag = tagRepository.findTagByName(existTagName);

        // then
        assertThat(notExistTag.isEmpty()).isEqualTo(true);
        assertThat(existTag.isPresent()).isEqualTo(true);
    }

    @Test
    public void findPostTagsByTag() throws Exception {
        // given
        Tag tag = newInstance(Tag.class);
        PostTag postTag1 = newInstance(PostTag.class);
        PostTag postTag2 = newInstance(PostTag.class);
        Post post1 = newInstance(Post.class);
        Post post2 = newInstance(Post.class);
        post1.addPostTag(postTag1);
        post2.addPostTag(postTag2);
        setField(postTag1, "tag", tag);
        setField(postTag2, "tag", tag);
        setField(postTag1, "post", post1);
        setField(postTag2, "post", post2);

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id"));

        postRepository.saveAll(List.of(post1, post2));
        tagRepository.save(tag);

        // when
        long totalElements = postTagRepository.findDistinctByTag(tag, pageRequest).getTotalElements();

        // then
        assertThat(totalElements).isEqualTo(2L);
    }

}
