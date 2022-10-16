package com.devillage.teamproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postLastModifiedAt;

    public Post(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.clicks = 0L;
        this.likeCount = 0L;
        this.postLastModifiedAt = LocalDateTime.of(0000, 12, 31, 00, 00,00,3333);
    }

    public void setClickCount(Long clickCount){
        this.clicks = clickCount;
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

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private final List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private final List<CommentLike> commentLikes = new ArrayList<>();

    public void addReportedPosts(ReportedPost reportedPost) {
        reportedPosts.add(reportedPost);
    }

    public void editPost(Post post){
        this.content = post.getContent();
        this.title = post.getTitle();
        this.postLastModifiedAt = LocalDateTime.now();
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

    public void addCommentLike(CommentLike commentLike){
        commentLikes.add(commentLike);
    }

    @Deprecated
    public void setDate() {
        setCreatedAt(LocalDateTime.now());
        setLastModifiedAt(LocalDateTime.now());
    }
}
