package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.response.ResFile;
import com.example.Job_Hunter.service.FileService;
import com.example.Job_Hunter.utill.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @Value("${upload-file.base-path}")
    private String basePath;
    @PostMapping("/files")
    public ResponseEntity<ResFile> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        if (file == null || file.isEmpty()) {
            throw new StorageException("Empty file");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream()
                .anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new StorageException("Invalid extension" + allowedExtensions);
        }
       fileService.createDir(basePath+folder);
       String uploadedFile = fileService.store(file, folder);
       ResFile resFile = new ResFile(uploadedFile, Instant.now());
       return ResponseEntity.ok().body(resFile);
    }

    @GetMapping("/files")
    public ResponseEntity<Resource> download(
            @RequestParam("file") String file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        if (file == null || folder == null) {
            throw new StorageException("Empty file");
        }
        long fileLength = fileService.getFileLength(file, folder);
        if (fileLength == 0) {
            throw new StorageException("File is empty");
        }
        InputStreamResource resource = fileService.getResource(file, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    }
