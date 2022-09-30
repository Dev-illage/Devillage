package com.devillage.teamproject.repository.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static com.devillage.teamproject.util.TestConstants.ID1;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("where와 페이지 같이 되는지 테스트")
    public void whereWithPage() throws Exception {
        // given
        int page = 0;
        int size = 10;
        Post post = Post.builder().build();
        postRepository.save(post);
        User user = User.builder().build();
        userRepository.save(user);
        Comment comment1 = Comment.builder().post(post).user(user).build();
        Comment comment2 = Comment.builder().post(post).user(user).build();
        Comment comment3 = Comment.builder().post(post).user(user).build();
        Comment comment4 = Comment.builder().post(post).user(user).build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        // when
        Page<Comment> commentPage = commentRepository.findAllByPostId(post.getId(), PageRequest.of(page, size));

        // then
        assertEquals(4, commentPage.getTotalElements());
    }
}