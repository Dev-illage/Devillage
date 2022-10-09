package com.devillage.teamproject.controller.file;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.FileDto;
import com.devillage.teamproject.security.resolver.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public interface FileController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    FileDto.Response postFile(@AccessToken AuthDto.UserInfo userInfo,
                  @RequestPart MultipartFile multipartFile);

    @GetMapping("/{file-id}")
    @ResponseStatus(HttpStatus.OK)
    FileDto.Response getFile(@PathVariable("file-id") Long id, @AccessToken AuthDto.UserInfo userInfo);

    @DeleteMapping("/{file-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFile(@AccessToken AuthDto.UserInfo userInfo, @PathVariable("file-id") Long fileId);
}
