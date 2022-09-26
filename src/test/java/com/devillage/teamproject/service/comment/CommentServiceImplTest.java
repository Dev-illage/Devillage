package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.comment.ReCommentRepository;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest implements Reflection {

    @Mock
    ReCommentRepository reCommentRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    public void deleteReComment() throws Exception {
        // given
        Post post = newInstance(Post.class);
        Comment comment = newInstance(Comment.class);
        ReComment reComment = newInstance(ReComment.class);

        setField(post, "id", 1L);
        setField(comment, "id", 2L);
        setField(reComment, "id", 3L);

        setField(reComment, "comment", comment);
        setField(comment, "post", post);

        doNothing().when(reCommentRepository).deleteById(anyLong());
        given(reCommentRepository.findById(reComment.getId()))
                .willReturn(Optional.of(reComment));
        given(reCommentRepository.findById(reComment.getId() + 1L))
                .willReturn(Optional.empty());

        // when / then
        assertDoesNotThrow(
                () -> commentService.deleteReComment(post.getId(), comment.getId(), reComment.getId()));
        assertThrows(BusinessLogicException.class,
                () -> commentService.deleteReComment(post.getId() + 1L, comment.getId(), reComment.getId()));
        assertThrows(BusinessLogicException.class,
                () -> commentService.deleteReComment(post.getId(), comment.getId() + 1L, reComment.getId()));
        assertThrows(BusinessLogicException.class,
                () -> commentService.deleteReComment(post.getId(), comment.getId(), reComment.getId() + 1L));
    }

}