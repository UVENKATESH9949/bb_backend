// src/main/java/com/BrainBlitz/controller/BulkImportController.java

package com.BrainBlitz.controller;

import com.BrainBlitz.dto.response.ApiResponse;
import com.BrainBlitz.dto.response.BulkImportResponse;
import com.BrainBlitz.service.BulkImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/questions/bulk")
public class BulkImportController {

	public BulkImportController(BulkImportService bulkImportService) {
        this.bulkImportService = bulkImportService;
    }
	
    private  BulkImportService bulkImportService;


    // ═════════════════════════════════════════════════════════════════
    // VALIDATE ENDPOINTS — no DB writes, just report
    // ═════════════════════════════════════════════════════════════════

    // Step 1a — validate JSON file before importing
    @PostMapping(value = "/validate/json",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BulkImportResponse>> validateJson(
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("JSON file is empty"));

        if (!isJsonFile(file))
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    "Invalid file type. Only .json files are allowed"));

        BulkImportResponse report = bulkImportService.validateJson(file);

        String message = report.getFailedCount() == 0
            ? "All " + report.getTotalSubmitted() + " questions are valid. Ready to import."
            : report.getFailedCount() + " question(s) have errors. Fix and re-upload.";

        return ResponseEntity.ok(ApiResponse.success(report, message));
    }

    // Step 1b — validate CSV file before importing
    @PostMapping(value = "/validate/csv",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BulkImportResponse>> validateCsv(
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("CSV file is empty"));

        if (!isCsvFile(file))
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    "Invalid file type. Only .csv files are allowed"));

        BulkImportResponse report = bulkImportService.validateCsv(file);

        String message = report.getFailedCount() == 0
            ? "All " + report.getTotalSubmitted() + " questions are valid. Ready to import."
            : report.getFailedCount() + " question(s) have errors. Fix and re-upload.";

        return ResponseEntity.ok(ApiResponse.success(report, message));
    }


    // ═════════════════════════════════════════════════════════════════
    // CONFIRM IMPORT ENDPOINTS — validates then saves to DB
    // ═════════════════════════════════════════════════════════════════

    // Step 2a — confirm JSON import
    @PostMapping(value = "/confirm/json",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BulkImportResponse>> confirmJsonImport(
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("JSON file is empty"));

        if (!isJsonFile(file))
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    "Invalid file type. Only .json files are allowed"));

        BulkImportResponse report =
            bulkImportService.confirmImport(file, "json");

        // If any rows failed — return 207 Multi-Status
        // so admin knows partial import happened
        HttpStatus status = report.getFailedCount() > 0
            ? HttpStatus.MULTI_STATUS
            : HttpStatus.CREATED;

        String message = buildImportMessage(report);

        return ResponseEntity
            .status(status)
            .body(ApiResponse.success(report, message));
    }

    // Step 2b — confirm CSV import
    @PostMapping(value = "/confirm/csv",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BulkImportResponse>> confirmCsvImport(
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("CSV file is empty"));

        if (!isCsvFile(file))
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    "Invalid file type. Only .csv files are allowed"));

        BulkImportResponse report =
            bulkImportService.confirmImport(file, "csv");

        HttpStatus status = report.getFailedCount() > 0
            ? HttpStatus.MULTI_STATUS
            : HttpStatus.CREATED;

        String message = buildImportMessage(report);

        return ResponseEntity
            .status(status)
            .body(ApiResponse.success(report, message));
    }


    // ═════════════════════════════════════════════════════════════════
    // TEMPLATE DOWNLOAD ENDPOINTS
    // ═════════════════════════════════════════════════════════════════

    // Download sample JSON template
    @GetMapping("/template/json")
    public ResponseEntity<byte[]> downloadJsonTemplate() {

        String template = bulkImportService.generateJsonTemplate();

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=bulk_import_template.json")
            .body(template.getBytes());
    }

    // Download sample CSV template
    @GetMapping("/template/csv")
    public ResponseEntity<byte[]> downloadCsvTemplate() {

        String template = bulkImportService.generateCsvTemplate();

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("text/csv"))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=bulk_import_template.csv")
            .body(template.getBytes());
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═════════════════════════════════════════════════════════════════

    private boolean isJsonFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        String type = file.getContentType();
        return (name != null && name.endsWith(".json"))
            || "application/json".equals(type);
    }

    private boolean isCsvFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        String type = file.getContentType();
        return (name != null && name.endsWith(".csv"))
            || "text/csv".equals(type)
            || "application/vnd.ms-excel".equals(type);
    }

    private String buildImportMessage(BulkImportResponse report) {
        if (report.getFailedCount() == 0) {
            return "All " + report.getSuccessCount()
                + " questions imported successfully";
        }
        return report.getSuccessCount() + " imported successfully, "
            + report.getFailedCount()
            + " failed. Check failedRows for details.";
    }
}



