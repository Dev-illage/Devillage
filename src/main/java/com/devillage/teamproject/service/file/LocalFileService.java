package com.devillage.teamproject.service.file;

import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.repository.file.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LocalFileService implements FileService {
    private final FileRepository fileRepository;

    public LocalFileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional
    public File saveFile(MultipartFile multipartFile) {
        return null;
    }

    @Override
    public File findFile() {
        return null;
    }

    @Override
    @Transactional
    public File editFile() {
        return null;
    }

    @Override
    public List<File> findFiles() {
        return null;
    }

    @Override
    @Transactional
    public void deleteFile() {

    }
}
