package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.File;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDto {

    //TODO : 임시 작성, 구현 시 주석 삭제
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long id;
        private String originalFileName;
        private Long fileSize;
        private String localPath;
        private String remotePath;
        private String type;
        private Long userId;

        public static Response of(File file) {
            return new Response(
                    file.getId(),
                    file.getOriginalFilename(),
                    file.getFileSize(),
                    file.getLocalPath(),
                    file.getRemotePath(),
                    file.getType(),
                    file.getOwner().getId()
            );
        }
    }
}
