package com.devillage.teamproject.entity;

import com.devillage.teamproject.entity.enums.FileType;
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
    private String originalFilename;

    private String filename;

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
    @Enumerated(EnumType.STRING)
    private FileType fileType;

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

    public static File createLocalImage(String originalFilename, String filename, Long fileSize,
                                        String localPath, StringBuffer requestURL) {
        File file = new File();
        file.originalFilename = originalFilename;
        file.filename = filename;
        file.fileSize = fileSize;
        file.localPath = localPath;
        file.fileType = FileType.IMAGE;
        file.remotePath = requestURL.append("?q=").append(file.getFilename()).toString();
        return file;
    }
}
