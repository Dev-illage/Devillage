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
@AllArgsConstructor
@Builder
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

    @ToString.Include
    @EqualsAndHashCode.Include
    private Long likeCount;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
        this.clicks = 0L;
        this.likeCount = 0L;
    }

    public void setLikeCount(Long num) {
        likeCount = num;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private final List<PostsFile> postsFile = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<PostTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private final List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @OneToMany(mappedBy = "post")
    private final List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<ReportedPost> reportedPosts = new ArrayList<>();

    public void addReportedPosts(ReportedPost reportedPost) {
        reportedPosts.add(reportedPost);
    }

    //외부 접근용(PostDto.Response) 생성자 추가
    public Post(Category category, String title, List<PostTag> tags, String content){
        this.title = title;
        this.content = content;
    }

    public void editPost(Post post){
        this.category = post.getCategory();
        this.content = post.getContent();
        this.title = post.getTitle();
        this.tags = post.getTags();
    }

    public void addPostTag(PostTag postTag) {
        this.tags.add(postTag);
    }

    public void addCategory(Category category) {
        this.category = category;
    }

    public void addUser(User user) {
        this.user = user;
    }

    @Deprecated
    public void setDate() {
        setCreatedAt(LocalDateTime.now());
        setLastModifiedAt(LocalDateTime.now());
    }
}
