package com.devillage.teamproject.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.file.FileRepository;
import com.devillage.teamproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class S3ImageService implements FileService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.dir}")
    private String folder;
    private final AmazonS3 amazonS3;
    private final UserService userService;
    private final FileRepository fileRepository;

    @Override
    public File saveFile(Long ownerUserId, MultipartFile multipartFile, StringBuffer requestURL) {
        User owner = userService.findVerifiedUser(ownerUserId);

        validateImage(multipartFile);

        String s3FileName = folder + "/" + UUID.randomUUID() + contentTypeToExtension(multipartFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());

        try {
            amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objectMetadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File file = File.createS3Image(multipartFile.getOriginalFilename(), s3FileName, multipartFile.getSize(),
                amazonS3.getUrl(bucket, s3FileName).toString());

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

    @Override
    public User addAvatarToUser(Long userId, MultipartFile imageFile, HttpServletRequest request) {
        User findUser = userService.findVerifiedUser(userId);
        File pastAvatar = findUser.getAvatar();
        File file = saveFile(userId, imageFile, request.getRequestURL());
        findUser.addAvatar(file);
        fileRepository.save(file);
        if (pastAvatar != null) {
            fileRepository.delete(pastAvatar);
        }
        return findUser;
    }

    @Override
    public void deleteUserAvatar(Long userId) {
        User findUser = userService.findVerifiedUser(userId);
        Long fileId = findUser.getAvatar().getId();
        findUser.addAvatar(null);
        deleteFile(fileId, userId);
    }

    public void validateImage(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.FILE_EMPTY);
        }

        String originalFilename = Optional.ofNullable(multipartFile.getOriginalFilename()).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.FILE_NAME_NOT_VALID)
        );

        if (!validateImageName(originalFilename)) {
            throw new BusinessLogicException(ExceptionCode.FILE_NAME_NOT_VALID);
        }

        String contentType = Optional.ofNullable(multipartFile.getContentType()).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.CONTENT_TYPE_NOT_ACCEPTABLE)
        );

        if (!List.of("image/jpeg", "image/png", "image/heic", "image/heif").contains(contentType)) {
            throw new BusinessLogicException(ExceptionCode.CONTENT_TYPE_NOT_ACCEPTABLE);
        }
    }

    public String contentTypeToExtension(MultipartFile multipartFile) {
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

        return fileExtension;
    }

    private boolean validateImageName(String originalFilename) {
        if (originalFilename.contains("%")) {
            return false;
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png") ||
                fileExtension.equals("heic") || fileExtension.equals("heif");
    }
}
