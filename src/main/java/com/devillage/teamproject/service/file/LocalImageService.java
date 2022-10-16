package com.devillage.teamproject.service.file;

import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.file.FileRepository;
import com.devillage.teamproject.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * 사진파일의 보안 조치 참고 : https://www.opswat.com/blog/file-upload-protection-best-practices
 * https://owin2828.github.io/devlog/2020/01/09/etc-2.html
 */
@Service
@Transactional
@Slf4j
public class LocalImageService implements FileService {
    private final FileRepository fileRepository;
    private final UserService userService;

    public LocalImageService(FileRepository fileRepository, UserService userService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
    }

    @Override
    public File saveFile(Long ownerUserId, MultipartFile multipartFile, StringBuffer requestURL) {
        User owner = userService.findVerifiedUser(ownerUserId);
        File file = parseMultipartFile(multipartFile, requestURL);
        file.addUser(owner);
        return fileRepository.save(file);
    }

    @Override
    @Transactional(readOnly = true)
    public File findFile(Long fileId) {
        return findVerifiedFile(fileId);
    }

    @Override
    public File editFile() {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<File> findFiles() {
        return null;
    }

    @Override
    public void deleteFile(Long fileId, Long userId) {
        File file = findVerifiedFile(fileId);
        if (!Objects.equals(file.getOwner().getId(), userId)) {
            throw new BusinessLogicException(ExceptionCode.USER_UNAUTHORIZED);
        }
        fileRepository.delete(file);
    }

    public File parseMultipartFile(MultipartFile multipartFile, StringBuffer requestURL) {
        if (multipartFile.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.FILE_EMPTY);
        }

        String path = "images" + java.io.File.separator + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        java.io.File dir = new java.io.File(path);
        if (!dir.exists()) {
            boolean mkdirResult = dir.mkdirs();
            if (!mkdirResult) {
                log.warn("mkdir was not successful");
            }
        }

        String originalFilename = Optional.ofNullable(multipartFile.getOriginalFilename()).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.FILE_NAME_NOT_VALID)
        );

        if (!validateFilename(originalFilename)) {
            throw new BusinessLogicException(ExceptionCode.FILE_NAME_NOT_VALID);
        }

        String contentType = Optional.ofNullable(multipartFile.getContentType()).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.CONTENT_TYPE_NOT_ACCEPTABLE)
        );

        String fileExtension;
        if (contentType.equals("image/jpeg")) {
            fileExtension = ".jpg";
        } else if (contentType.equals("image/png")) {
            fileExtension = ".png";
        } else if (contentType.equals("image/heic")) {
            fileExtension = ".heic";
        } else if (contentType.equals("image/heif")) {
            fileExtension = ".heif";
        } else {
            throw new BusinessLogicException(ExceptionCode.CONTENT_TYPE_NOT_ACCEPTABLE);
        }

        UUID uuid = UUID.randomUUID();
        String newFilename = uuid + fileExtension;

        File file = File.createLocalImage(originalFilename, newFilename, multipartFile.getSize(),
                path + java.io.File.separator + newFilename, requestURL);

        String absolutePath = new java.io.File("").getAbsolutePath() + java.io.File.separator;

        java.io.File localFile = new java.io.File(absolutePath + path + java.io.File.separator +
                newFilename);
        try {
            multipartFile.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        localFile.setWritable(true);
        localFile.setReadable(true);

        return file;
    }

    private boolean validateFilename(String originalFilename) {
        if (originalFilename.contains("%")) {
            return false;
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png") ||
                fileExtension.equals("heic") || fileExtension.equals("heif");
    }

    @Override
    @Transactional(readOnly = true)
    public File findFileWithFilename(String filename) {
        return fileRepository.findByFilename(filename).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.FILE_NOT_FOUND)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public File findVerifiedFile(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.FILE_NOT_FOUND)
        );
    }
}
