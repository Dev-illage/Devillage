package com.devillage.teamproject.entity;

import com.devillage.teamproject.entity.enums.CommentStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@Builder
public class Comment extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="comment_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Setter
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment")
    private final List<ReComment> reComments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    public static Comment createComment(Comment comment, User user, Post post) {
        Comment newComment = Comment.builder()
                .content(comment.getContent())
                .user(user)
                .post(post)
                .commentStatus(CommentStatus.VALID)
                .build();
        user.addComment(newComment);
        post.addComment(newComment);

        return newComment;
    }

    public void deleteComment() {
        this.commentStatus = CommentStatus.DELETED;
    }
}
