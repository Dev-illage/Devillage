package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.entity.enums.FileType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class Response {
        private Long id;
        private String originalFilename;
        private String filename;
        private Long fileSize;
        private String localPath;
        private String remotePath;
        private FileType fileType;
        private Long ownerUserId;

        public static Response of(File file) {
            return Response.builder()
                    .id(file.getId())
                    .originalFilename(file.getOriginalFilename())
                    .filename(file.getFilename())
                    .fileSize(file.getFileSize())
                    .localPath(file.getLocalPath())
                    .remotePath(file.getRemotePath())
                    .fileType(file.getFileType())
                    .ownerUserId(file.getOwner().getId())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostResponse {
        private Long id;
        private String filename;
        private String remotePath;

        public static PostResponse of(File file) {
            return PostResponse.builder()
                    .id(file.getId())
                    .filename(file.getFilename())
                    .remotePath(file.getRemotePath())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleResponse {
        private String filename;
        private String remotePath;

        public static SimpleResponse of(File file) {
            return SimpleResponse.builder()
                    .filename(file.getFilename())
                    .remotePath(file.getRemotePath())
                    .build();
        }
    }
}
