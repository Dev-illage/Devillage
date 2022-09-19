package com.devillage.teamproject.service.file;

import com.devillage.teamproject.entity.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FileService {
    File saveFile();

    File findFile();

    File editFile();

    List<File> findFiles();

    void deleteFile();
}
