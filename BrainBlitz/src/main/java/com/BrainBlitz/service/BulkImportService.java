// src/main/java/com/BrainBlitz/service/BulkImportService.java

package com.BrainBlitz.service;

import com.BrainBlitz.dto.response.BulkImportResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BulkImportService {

    // Validate only — no DB writes
    BulkImportResponse validateJson(MultipartFile file);
    BulkImportResponse validateCsv(MultipartFile file);

    // Save to DB after validation
    BulkImportResponse confirmImport(MultipartFile file, String fileType);

    // Template downloads
    String generateJsonTemplate();
    String generateCsvTemplate();
}