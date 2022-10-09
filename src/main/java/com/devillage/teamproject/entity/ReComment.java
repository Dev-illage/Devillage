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
@AllArgsConstructor
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
    private List<ReComment> reComments = new ArrayList<>();

    public static ReComment createReComment(User user, Comment comment, String content) {
        return ReComment.builder()
                .user(user).comment(comment).content(content)
                .build();
    }
}
