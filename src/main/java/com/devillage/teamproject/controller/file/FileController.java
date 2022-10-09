package com.devillage.teamproject.controller.file;

import com.devillage.teamproject.dto.FileDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
public interface FileController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    Long postFile();

    @GetMapping("/{file-id}")
    @ResponseStatus(HttpStatus.OK)
    FileDto.Response getFile(@PathVariable("file-id") String id);

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFile();
}
