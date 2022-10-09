package com.devillage.teamproject.service.file;

import com.devillage.teamproject.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File saveFile(Long ownerUserId, MultipartFile multipartFile);

    File findFile(Long fileId);

    File editFile();

    List<File> findFiles();

    void deleteFile();

    File findFileWithFilename(String filename);

    File findVerifiedFile(Long fileId);
}
