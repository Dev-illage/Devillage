package com.devillage.teamproject.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.repository.file.FileRepository;
import com.devillage.teamproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
public class S3ImageService implements FileService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final UserService userService;
    private final FileRepository fileRepository;

    @Override
    public File saveFile(Long ownerUserId, MultipartFile multipartFile, StringBuffer requestURL) {
        return null;
    }

    @Override
    public File findFile(Long fileId) {
        return null;
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

    }

    @Override
    public File findFileWithFilename(String filename) {
        return null;
    }

    @Override
    public File findVerifiedFile(Long fileId) {
        return null;
    }

    @Override
    public User addAvatarToUser(Long userId, MultipartFile imageFile, HttpServletRequest request) {
        return null;
    }

    @Override
    public void deleteUserAvatar(Long userId) {

    }
}
