package com.devillage.teamproject.service.file;

import com.devillage.teamproject.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File saveFile(MultipartFile multipartFile);

    File findFile();

    File editFile();

    List<File> findFiles();

    void deleteFile();
}
