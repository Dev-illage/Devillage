package com.devillage.teamproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder
public class ReComment extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="re_comment_id")
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
    private Comment comment;

    @OneToMany(mappedBy = "reComment")
    private List<ReCommentLike> reCommentLikes = new ArrayList<>();

    @Builder
    public ReComment(Long id, String content, User user, Comment comment, List<ReCommentLike> reCommentLikes) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.comment = comment;
        this.reCommentLikes = reCommentLikes == null ? new ArrayList<>() : reCommentLikes;
    }

    public static ReComment createReComment(User user, Comment comment, String content) {
        return ReComment.builder()
                .user(user).comment(comment).content(content)
                .build();
    }
}
