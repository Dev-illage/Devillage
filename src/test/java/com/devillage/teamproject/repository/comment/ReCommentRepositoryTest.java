package com.devillage.teamproject.repository.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReCommentRepositoryTest implements Reflection {

    @Autowired
    ReCommentRepository reCommentRepository;

    @Test
    public void saveAndDelete() throws Exception {
        // given
        Post post = newInstance(Post.class);
        Comment comment = newInstance(Comment.class);
        ReComment reComment = newInstance(ReComment.class);

        setField(reComment, "comment", comment);
        setField(comment, "post", post);

        reCommentRepository.save(reComment);

        // when / then
        reCommentRepository.deleteById(reComment.getId());
        assertThrows(EmptyResultDataAccessException.class,
                () -> reCommentRepository.deleteById(reComment.getId()));
    }

}