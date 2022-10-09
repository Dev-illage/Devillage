package com.devillage.teamproject.controller.file;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.FileDto;
import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.service.file.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class LocalImageController implements FileController {

    private final FileService fileService;

    public LocalImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public FileDto.Response postFile(AuthDto.UserInfo userInfo, MultipartFile multipartFile) {
        File savedFile = fileService.saveFile(userInfo.getId(), multipartFile);
        return FileDto.Response.of(savedFile);
    }

    @Override
    public FileDto.Response getFile(Long id, AuthDto.UserInfo userInfo) {
        File findFile = fileService.findFile(id);
        if (!Objects.equals(findFile.getOwner().getId(), userInfo.getId())) {
            throw new BusinessLogicException(ExceptionCode.USER_UNAUTHORIZED);
        }
        return FileDto.Response.of(findFile);
    }

    @Override
    public void deleteFile(AuthDto.UserInfo userInfo, Long fileId) {
        fileService.deleteFile(fileId, userInfo.getId());
    }

    @GetMapping()
    public ResponseEntity<Resource> findFileWithFilename(@RequestParam(name = "q") String filename) throws IOException {
        File findFile = fileService.findFileWithFilename(filename);
        String absolutePath = new java.io.File("").getAbsolutePath() + java.io.File.separator;

        Resource resource = new FileSystemResource(absolutePath + findFile.getLocalPath());

        if (!resource.exists()) {
            throw new BusinessLogicException(ExceptionCode.FILE_NOT_FOUND);
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                .filename(findFile.getOriginalFilename()).build();

        HttpHeaders headers = new HttpHeaders();
        Path filePath = Paths.get(findFile.getLocalPath());
        headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath));
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
