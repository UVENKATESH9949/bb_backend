// src/main/java/com/BrainBlitz/controller/FileUploadController.java

package com.BrainBlitz.controller;

import com.BrainBlitz.dto.response.ApiResponse;
import com.BrainBlitz.service.FileUploadService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/upload")
public class FileUploadController {
	
	private FileUploadService filUploadService;
	
	public FileUploadController(FileUploadService fileUploadService) {
        this.filUploadService = fileUploadService;
    }
	
    // ═════════════════════════════════════════════════════════════════
    // IMAGE UPLOAD ENDPOINTS
    // ═════════════════════════════════════════════════════════════════

    // Single image upload
    // Returns Cloudinary URL — admin copies and uses in question body
    @PostMapping(value = "/image",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Image file is empty"));

        String imageUrl = filUploadService.uploadImage(file);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                Map.of(
                    "imageUrl",  imageUrl,
                    "fileName",  file.getOriginalFilename(),
                    "size",      formatFileSize(file.getSize())
                ),
                "Image uploaded successfully"));
    }

    // Bulk image upload — accepts ZIP file
    // Returns map of filename → Cloudinary URL
    // Admin uses this map to link images to questions
    @PostMapping(value = "/images/bulk",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadBulkImages(
            @RequestPart("file") MultipartFile zipFile) {

        if (zipFile.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("ZIP file is empty"));

        String fileName = zipFile.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".zip"))
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    "Invalid file type. Only .zip files are allowed"));

        Map<String, String> uploadedFiles =
        		filUploadService.uploadBulkImages(zipFile);

        // Count successes and failures
        long successCount = uploadedFiles.values().stream()
            .filter(url -> !url.equals("UPLOAD_FAILED"))
            .count();
        long failedCount = uploadedFiles.size() - successCount;

        String message = failedCount == 0
            ? "All " + successCount + " images uploaded successfully"
            : successCount + " uploaded, "
                + failedCount + " failed. Check map for UPLOAD_FAILED entries.";

        HttpStatus status = failedCount > 0
            ? HttpStatus.MULTI_STATUS
            : HttpStatus.CREATED;

        return ResponseEntity
            .status(status)
            .body(ApiResponse.success(uploadedFiles, message));
    }


    // ═════════════════════════════════════════════════════════════════
    // AUDIO UPLOAD ENDPOINT
    // ═════════════════════════════════════════════════════════════════

    // Audio upload — for listening_comp questions
    // Returns Cloudinary audio URL
    @PostMapping(value = "/audio",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadAudio(
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Audio file is empty"));

        String audioUrl = filUploadService.uploadAudio(file);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                Map.of(
                    "audioUrl",  audioUrl,
                    "fileName",  file.getOriginalFilename(),
                    "size",      formatFileSize(file.getSize())
                ),
                "Audio uploaded successfully"));
    }


    // ═════════════════════════════════════════════════════════════════
    // DELETE ENDPOINT
    // ═════════════════════════════════════════════════════════════════

    // Delete file from Cloudinary by URL
    // Used when admin replaces an image or audio
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @RequestParam String fileUrl) {

        if (fileUrl == null || fileUrl.isBlank())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("fileUrl is required"));

        filUploadService.deleteFile(fileUrl);

        return ResponseEntity.ok(
            ApiResponse.success(null, "File deleted successfully"));
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═════════════════════════════════════════════════════════════════

    // Converts bytes to human readable format
    private String formatFileSize(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        else if (bytes < 1024 * 1024)
            return String.format("%.1f KB", bytes / 1024.0);
        else
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }
}