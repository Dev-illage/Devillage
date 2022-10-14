package com.devillage.teamproject.entity;

import com.devillage.teamproject.entity.enums.CommentStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
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

    @ToString.Include
    @EqualsAndHashCode.Include
    private Long likeCount;

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    @OneToMany(mappedBy = "comment",cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<CommentLike> commentLikes = new ArrayList<>();

    @Builder
    public Comment(Long id, String content, User user, Post post, Long likeCount, CommentStatus commentStatus, List<CommentLike> commentLikes) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.post = post;
        this.likeCount = Optional.ofNullable(likeCount).orElse(0L);
        this.commentStatus = Optional.ofNullable(commentStatus).orElse(CommentStatus.VALID);
        this.commentLikes = Optional.ofNullable(commentLikes).orElse(new ArrayList<>());
        this.setCreatedAt(LocalDateTime.now());
        this.setLastModifiedAt(LocalDateTime.now());
    }

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

    public void setLikeCount(Long count) {
        likeCount = count;
    }

    public void deleteComment() {
        this.commentStatus = CommentStatus.DELETED;
    }
}
