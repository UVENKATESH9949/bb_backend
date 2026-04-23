package com.BrainBlitz.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface FileUploadService {

    String uploadImage(MultipartFile file);
    Map<String, String> uploadBulkImages(MultipartFile zipFile);
    String uploadAudio(MultipartFile file);
    void deleteFile(String fileUrl);
}