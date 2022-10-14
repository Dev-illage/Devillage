package com.devillage.teamproject.service.file;

import com.devillage.teamproject.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File saveFile(Long ownerUserId, MultipartFile multipartFile, StringBuffer requestURL);

    File findFile(Long fileId);

    File editFile();

    List<File> findFiles();

    void deleteFile(Long fileId, Long userId);

    File findFileWithFilename(String filename);

    File findVerifiedFile(Long fileId);
}
