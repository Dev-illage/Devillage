package com.devillage.teamproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class File extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="file_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String originalFileName;

    @ToString.Include
    @EqualsAndHashCode.Include
    private Long fileSize;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String localPath;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String remotePath;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User owner;

    public static File ofOauth2UserPicture(String path) {
        File file = new File();
        file.localPath = path;
        return file;
    }

    public void addUser(User user) {
        this.owner = user;
    }
}
