package com.devillage.teamproject.entity;

import com.devillage.teamproject.entity.enums.ReportType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ReportedPost extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="reported_post_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public ReportedPost(User user, Post post, ReportType reportType, String content) {
        this.user = user;
        this.post = post;
        this.reportType = reportType;
        this.content = content;
    }
}
