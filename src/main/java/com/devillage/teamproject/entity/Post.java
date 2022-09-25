package com.devillage.teamproject.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Post extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "post_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String title;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String content;

    @ToString.Include
    @EqualsAndHashCode.Include
    private Long clicks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<PostsFile> postsFile = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<ReportedPost> reportedPosts = new ArrayList<>();

    public void addReportedPosts(ReportedPost reportedPost) {
        reportedPosts.add(reportedPost);
    }

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    //외부 접근용(PostDto.Response) 생성자 추가
    public Post(Category category,String title,List<PostTag> tags,String content){
        this.category = category;
        this.title = title;
        this.tags = tags;
        this.content = content;
    }

    public void edit(Post post){
        this.category = post.getCategory();
        this.content = post.getContent();
        this.title = post.getTitle();
        this.tags = post.getTags();
    }


}
