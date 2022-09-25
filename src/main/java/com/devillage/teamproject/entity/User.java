package com.devillage.teamproject.entity;

import com.devillage.teamproject.entity.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="user_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String email;

    private String password;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String nickName;

    @OneToMany(mappedBy = "user")
    private List<UserRoles> userRoles = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File avatar;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pwdLastModifiedAt;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @ToString.Include
    private Long point;

    @ToString.Include
    private String statusMessage;

    private Boolean authenticatedMail;

    @ToString.Include
    private String oauthProvider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    public void addBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
    }

    @OneToMany(mappedBy = "user")
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Like> likes = new ArrayList<>();

    public void addLike(Like like) {
        likes.add(like);
    }

    @OneToMany(mappedBy = "user")
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<ReComment> reComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private User blocker;

    @OneToMany(mappedBy = "blocker")
    private final List<User> blockedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<ReportedPost> reportedPosts = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.pwdLastModifiedAt = LocalDateTime.now();
    }

    public void passwordEncryption(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public boolean passwordVerification(PasswordEncoder passwordEncoder, User user) {
        return passwordEncoder.matches(user.getPassword(), this.getPassword());
    }

    public void deleteUser() {
        this.userStatus = UserStatus.RESIGNED;
    }
}
