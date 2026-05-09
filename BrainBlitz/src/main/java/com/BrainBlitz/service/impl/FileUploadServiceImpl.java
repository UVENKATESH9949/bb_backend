// src/main/java/com/BrainBlitz/service/impl/FileUploadServiceImpl.java

package com.BrainBlitz.service.impl;

import com.BrainBlitz.service.FileUploadService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary = new Cloudinary();

    @Value("${cloudinary.folder.images}")
    private String imagesFolder;

    @Value("${cloudinary.folder.audio}")
    private String audioFolder;

    // Allowed image types
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
        "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    // Allowed audio types
    private static final Set<String> ALLOWED_AUDIO_TYPES = Set.of(
        "audio/mpeg", "audio/mp3", "audio/wav", "audio/ogg"
    );

    private static final org.slf4j.Logger log =
    	    org.slf4j.LoggerFactory.getLogger(FileUploadServiceImpl.class);
    // Max file sizes
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;   // 5MB
    private static final long MAX_AUDIO_SIZE = 50 * 1024 * 1024;  // 50MB
    private static final long MAX_ZIP_SIZE   = 100 * 1024 * 1024; // 100MB


    // ═════════════════════════════════════════════════════════════════
    // UPLOAD METHODS
    // ═════════════════════════════════════════════════════════════════

    @Override
    public String uploadImage(MultipartFile file) {

        validateFile(file, ALLOWED_IMAGE_TYPES, MAX_IMAGE_SIZE, "Image");

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder",          imagesFolder,
                    "resource_type",   "image",
                    "use_filename",    true,
                    "unique_filename", true
                )
            );

            String url = (String) result.get("secure_url");
            log.info("Image uploaded successfully: {}", url);
            return url;

        } catch (IOException e) {
            log.error("Image upload failed: {}", e.getMessage());
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> uploadBulkImages(MultipartFile zipFile) {

        // Validate ZIP file
        if (zipFile.isEmpty())
            throw new IllegalArgumentException("ZIP file is empty");
        if (zipFile.getSize() > MAX_ZIP_SIZE)
            throw new IllegalArgumentException("ZIP file exceeds 100MB limit");

        Map<String, String> uploadedFiles = new LinkedHashMap<>();
        List<String> failedFiles         = new ArrayList<>();

        // Extract ZIP and upload each image
        try (ZipInputStream zipStream = new ZipInputStream(
                zipFile.getInputStream())) {

            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {

                // Skip directories and hidden files
                if (entry.isDirectory()
                        || entry.getName().startsWith("__MACOSX")
                        || entry.getName().startsWith(".")) {
                    zipStream.closeEntry();
                    continue;
                }

                String fileName = Paths.get(entry.getName())
                                       .getFileName()
                                       .toString();

                // Only process image files
                if (!isImageFile(fileName)) {
                    log.warn("Skipping non-image file: {}", fileName);
                    zipStream.closeEntry();
                    continue;
                }

                try {
                    // Read entry bytes
                    byte[] fileBytes = zipStream.readAllBytes();

                    // Upload to Cloudinary
                    Map<?, ?> result = cloudinary.uploader().upload(
                        fileBytes,
                        ObjectUtils.asMap(
                            "folder",          imagesFolder,
                            "resource_type",   "image",
                            "public_id",       stripExtension(fileName),
                            "unique_filename", true
                        )
                    );

                    String url = (String) result.get("secure_url");
                    uploadedFiles.put(fileName, url);
                    log.info("Bulk upload — uploaded: {}", fileName);

                } catch (Exception e) {
                    log.error("Bulk upload — failed for {}: {}",
                        fileName, e.getMessage());
                    failedFiles.add(fileName);
                }

                zipStream.closeEntry();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read ZIP file: "
                + e.getMessage());
        }

        // Add failed files to result map with ERROR marker
        // so admin knows which ones failed
        failedFiles.forEach(f -> uploadedFiles.put(f, "UPLOAD_FAILED"));

        if (uploadedFiles.isEmpty())
            throw new RuntimeException(
                "No valid image files found in ZIP");

        log.info("Bulk upload complete — success: {}, failed: {}",
            uploadedFiles.size() - failedFiles.size(),
            failedFiles.size());

        return uploadedFiles;
    }

    @Override
    public String uploadAudio(MultipartFile file) {

        validateFile(file, ALLOWED_AUDIO_TYPES, MAX_AUDIO_SIZE, "Audio");

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder",          audioFolder,
                    "resource_type",   "video",  // Cloudinary uses "video" for audio
                    "use_filename",    true,
                    "unique_filename", true
                )
            );

            String url = (String) result.get("secure_url");
            log.info("Audio uploaded successfully: {}", url);
            return url;

        } catch (IOException e) {
            log.error("Audio upload failed: {}", e.getMessage());
            throw new RuntimeException("Audio upload failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileUrl) {

        if (fileUrl == null || fileUrl.isBlank())
            throw new IllegalArgumentException("File URL is required");

        try {
            // Extract public_id from Cloudinary URL
            // URL format: https://res.cloudinary.com/{cloud}/image/upload/v123/{folder}/{publicId}.ext
            String publicId = extractPublicId(fileUrl);

            cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.emptyMap()
            );

            log.info("File deleted from Cloudinary: {}", publicId);

        } catch (Exception e) {
            log.error("File deletion failed for URL {}: {}",
                fileUrl, e.getMessage());
            throw new RuntimeException("File deletion failed: " + e.getMessage());
        }
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE HELPER METHODS
    // ═════════════════════════════════════════════════════════════════

    private void validateFile(MultipartFile file,
                               Set<String> allowedTypes,
                               long maxSize,
                               String fileLabel) {

        if (file == null || file.isEmpty())
            throw new IllegalArgumentException(
                fileLabel + " file is empty");

        if (file.getSize() > maxSize)
            throw new IllegalArgumentException(
                fileLabel + " file exceeds size limit of "
                + (maxSize / (1024 * 1024)) + "MB");

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType))
            throw new IllegalArgumentException(
                "Invalid " + fileLabel.toLowerCase()
                + " type: " + contentType
                + ". Allowed: " + allowedTypes);
    }

    private boolean isImageFile(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg")
            || lower.endsWith(".jpeg")
            || lower.endsWith(".png")
            || lower.endsWith(".webp")
            || lower.endsWith(".gif");
    }

    private String stripExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    // Extracts Cloudinary public_id from a secure URL
    // e.g. "brainblitz/images/abc123" from full URL
    private String extractPublicId(String url) {
        // Remove everything up to and including "/upload/"
        String afterUpload = url.substring(url.indexOf("/upload/") + 8);

        // Remove version prefix if present (e.g. "v1234567890/")
        if (afterUpload.startsWith("v") && afterUpload.contains("/")) {
            afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
        }

        // Remove file extension
        return stripExtension(afterUpload);
    }
}
