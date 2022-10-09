package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.entity.enums.FileType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDto {

    //TODO : 임시 작성, 구현 시 주석 삭제
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
                    .localPath(file.getLocalPath())
                    .remotePath(file.getRemotePath())
                    .fileType(file.getFileType())
                    .ownerUserId(file.getOwner().getId())
                    .build();
        }
    }
}
