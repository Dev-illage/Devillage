package com.devillage.teamproject.repository.file;

import com.devillage.teamproject.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFilename(String filename);
}
