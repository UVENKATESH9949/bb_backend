// src/main/java/com/BrainBlitz/service/impl/BulkImportServiceImpl.java

package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.request.*;
import com.BrainBlitz.dto.response.BulkImportResponse;
import com.BrainBlitz.enums.*;
import com.BrainBlitz.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Locale;


@Service
public class BulkImportServiceImpl implements BulkImportService {

    private final QuestionService questionService;
    private final ObjectMapper objectMapper;

    public BulkImportServiceImpl(QuestionService questionService, ObjectMapper objectMapper) {
        this.questionService = questionService;
        this.objectMapper = objectMapper;
    }
    
    private static final org.slf4j.Logger log =
    	    org.slf4j.LoggerFactory.getLogger(BulkImportServiceImpl.class);

    private static final Set<String> OPTION_BASED_TYPES = Set.of(
        "MCQ", "MULTI_CORRECT", "TRUE_FALSE", "SYNONYM_ANTONYM", "ERROR_SPOTTING",
        "READING_COMPREHENSION", "CLOZE_TEST", "TABLE_DI", "BAR_CHART_DI", "PIE_CHART_DI", "LINE_GRAPH_DI", "CASELET_DI"
    );
    
    // ═════════════════════════════════════════════════════════════════
    // VALIDATE METHODS — no DB writes
    // ═════════════════════════════════════════════════════════════════

    @Override
    public BulkImportResponse validateJson(MultipartFile file) {
        List<Map<String, Object>> questions = parseJsonFile(file);
        return validateQuestions(questions, true);
    }

    @Override
    public BulkImportResponse validateCsv(MultipartFile file) {
        List<Map<String, Object>> questions = parseCsvFile(file);
        return validateQuestions(questions, true);
    }


    // ═════════════════════════════════════════════════════════════════
    // CONFIRM IMPORT — validates then saves to DB
    // ═════════════════════════════════════════════════════════════════

    @Override
    public BulkImportResponse confirmImport(MultipartFile file, String fileType) {

        List<Map<String, Object>> questions = fileType.equalsIgnoreCase("csv")
            ? parseCsvFile(file)
            : parseJsonFile(file);

        // Validate first
        BulkImportResponse validationReport = validateQuestions(questions, false);

        // Stop if any rows failed validation
        if (validationReport.getFailedCount() > 0) {
            validationReport.setIsValidationOnly(false);
            return validationReport;
        }

        // All valid — save each record
        List<BulkImportResponse.FailedRow> failedRows = new ArrayList<>();
        int successCount = 0;
        Map<String, Long> groupKeyToId = new HashMap<>();

        for (int i = 0; i < questions.size(); i++) {
            Map<String, Object> raw = questions.get(i);
            try {
                routeAndSave(raw, groupKeyToId);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to save row {}: {}", i + 1, e.getMessage());
                failedRows.add(new BulkImportResponse.FailedRow(
                	    i + 1,
                	    truncate((String) raw.get("questionText")),
                	    "Save failed: " + e.getMessage()
                	));
            }
        }

        BulkImportResponse response = new BulkImportResponse();
        response.setTotalSubmitted(questions.size());
        response.setSuccessCount(successCount);
        response.setFailedCount(failedRows.size());
        response.setIsValidationOnly(false);
        response.setFailedRows(failedRows);
        return response;
    }


    // ═════════════════════════════════════════════════════════════════
    // TEMPLATE GENERATORS
    // ═════════════════════════════════════════════════════════════════

    @Override
    public String generateJsonTemplate() {
        List<Map<String, Object>> template = new ArrayList<>();

        Map<String, Object> sampleMcq = new LinkedHashMap<>();
        sampleMcq.put("questionType", "MCQ");
        sampleMcq.put("questionText", "A shopkeeper bought an article for ₹500 and sold it for ₹625. What is his profit percentage?");
        sampleMcq.put("examCategory", "GOVT_EXAM");
        sampleMcq.put("examType", "SSC_CGL");
        sampleMcq.put("subject", "Quantitative Aptitude");
        sampleMcq.put("topic", "Profit and Loss");
        sampleMcq.put("difficultyLevel", "LEVEL_2");
        sampleMcq.put("language", "ENGLISH");
        sampleMcq.put("marks", 2.0);
        sampleMcq.put("negativeMarks", 0.5);
        sampleMcq.put("hint", "Profit% = (Profit / CP) × 100");
        sampleMcq.put("isAiGenerated", false);
        sampleMcq.put("groupId", null);

        // questionExplanation — Layer 1 + Layer 2
        Map<String, Object> explanation = new LinkedHashMap<>();
        explanation.put("whatIsAsked", "Find the profit percentage earned by the shopkeeper.");
        explanation.put("whatIsGiven", "Cost Price = ₹500, Selling Price = ₹625.");
        explanation.put("whatApproach", "First calculate profit, then apply the profit percentage formula.");
        explanation.put("howToSolve", "Profit = SP - CP = 625 - 500 = 125. Then Profit% = (125 / 500) × 100 = 25%.");

        List<Map<String, Object>> methods = new ArrayList<>();
        Map<String, Object> m1 = new LinkedHashMap<>();
        m1.put("title", "Standard Formula Method");
        m1.put("description", "Use Profit = SP - CP, then Profit% = (Profit / CP) × 100.");
        m1.put("displayOrder", 1);
        m1.put("isActive", true);

        Map<String, Object> m2 = new LinkedHashMap<>();
        m2.put("title", "Shortcut Method");
        m2.put("description", "Profit% = ((SP - CP) / CP) × 100 = (125 / 500) × 100 = 25%.");
        m2.put("displayOrder", 2);
        m2.put("isActive", true);

        methods.add(m1);
        methods.add(m2);
        explanation.put("methods", methods);

        sampleMcq.put("questionExplanation", explanation);

        List<Map<String, Object>> options = new ArrayList<>();
        options.add(Map.of("optionText", "20%", "isCorrect", false, "optionOrder", 1));
        options.add(Map.of("optionText", "25%", "isCorrect", true,  "optionOrder", 2));
        options.add(Map.of("optionText", "30%", "isCorrect", false, "optionOrder", 3));
        options.add(Map.of("optionText", "15%", "isCorrect", false, "optionOrder", 4));
        sampleMcq.put("options", options);

        template.add(sampleMcq);

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON template");
        }
    }

    @Override
    public String generateCsvTemplate() {
        // CSV is flat — questionExplanation fields are added as separate columns
        // methods are not supported in CSV (use JSON for multi-method questions)
        return "questionType,questionText,examCategory,examType,subject," +
               "topic,difficultyLevel,language,marks,negativeMarks,hint,isAiGenerated," +
               "whatIsAsked,whatIsGiven,whatApproach,howToSolve," +
               "option1,option2,option3,option4,correctOption\n" +
               "MCQ,\"A shopkeeper bought an article for 500 and sold for 625. Profit%?\"," +
               "GOVT_EXAM,SSC_CGL,Quantitative Aptitude,Profit and Loss," +
               "LEVEL_2,ENGLISH,2.0,0.5,\"Profit% = (Profit / CP) x 100\",false," +
               "\"Find the profit percentage.\",\"CP=500 SP=625.\",\"Apply profit% formula.\"," +
               "\"Profit=125. Profit%=(125/500)x100=25%.\"," +
               "20%,25%,30%,15%,2\n";
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE — PARSE METHODS
    // ═════════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseJsonFile(MultipartFile file) {
        try {
            Object root = objectMapper.readValue(file.getInputStream(), Object.class);
            if (root instanceof List<?> list) {
                return list.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<String, Object>) item)
                    .toList();
            }
            if (root instanceof Map<?, ?> mapRoot) {
                List<Map<String, Object>> records = new ArrayList<>();
                List<Map<String, Object>> groups = toMapList(mapRoot.get("questionGroups"));
                if (groups.isEmpty()) {
                    groups = toMapList(mapRoot.get("groups"));
                }
                for (Map<String, Object> group : groups) {
                    Map<String, Object> row = new LinkedHashMap<>(group);
                    row.put("__recordType", "GROUP");
                    records.add(row);
                }
                List<Map<String, Object>> questions = toMapList(mapRoot.get("questions"));
                if (questions.isEmpty()) {
                    questions = toMapList(mapRoot.get("records"));
                }
                for (Map<String, Object> q : questions) {
                    Map<String, Object> row = new LinkedHashMap<>(q);
                    row.putIfAbsent("__recordType", "QUESTION");
                    records.add(row);
                }
                return records;
            }
            throw new IllegalArgumentException("JSON root must be an array or object");
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Invalid JSON file format: " + e.getMessage());
        }
    }

    private List<Map<String, Object>> parseCsvFile(MultipartFile file) {
        List<Map<String, Object>> result = new ArrayList<>();
        try (
            Reader reader = new InputStreamReader(
                file.getInputStream(), StandardCharsets.UTF_8);
            CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreSurroundingSpaces(true)
                    .build())
        ) {
            for (CSVRecord record : csvParser) {
                Map<String, Object> row = new LinkedHashMap<>(record.toMap());
                row.put("__recordType", "QUESTION");
                row.put("questionType", normalizeQuestionType((String) row.get("questionType")));

                // Build options list from flat CSV columns
                String qType = (String) row.get("questionType");
                if (OPTION_BASED_TYPES.contains(qType)) {
                    List<Map<String, Object>> options = new ArrayList<>();
                    Integer correctOption = null;
                    if (record.isMapped("correctOption")
                            && !record.get("correctOption").isBlank()) {
                        correctOption = Integer.parseInt(record.get("correctOption").trim());
                    }
                    for (int i = 1; i <= 6; i++) {
                        String key = "option" + i;
                        if (record.isMapped(key) && !record.get(key).isBlank()) {
                            Map<String, Object> option = new LinkedHashMap<>();
                            option.put("optionText", record.get(key));
                            option.put("isCorrect", correctOption != null && i == correctOption);
                            option.put("optionOrder", i);
                            options.add(option);
                        }
                    }
                    row.put("options", options);
                }
                result.add(row);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Invalid CSV file format: " + e.getMessage());
        }
        return result;
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE — VALIDATE METHOD
    // ═════════════════════════════════════════════════════════════════

    private BulkImportResponse validateQuestions(
            List<Map<String, Object>> questions,
            boolean isValidationOnly) {

        List<BulkImportResponse.FailedRow> failedRows = new ArrayList<>();
        int successCount = 0;

        for (int i = 0; i < questions.size(); i++) {
            Map<String, Object> raw = questions.get(i);
            String reason = validateSingleRecord(raw);

            if (reason != null) {
            	failedRows.add(new BulkImportResponse.FailedRow(
            		    i + 1,
            		    truncate((String) raw.get("questionText")),
            		    reason
            		));
            } else {
                successCount++;
            }
        }

        BulkImportResponse response = new BulkImportResponse();
        response.setTotalSubmitted(questions.size());
        response.setSuccessCount(successCount);
        response.setFailedCount(failedRows.size());
        response.setIsValidationOnly(isValidationOnly);
        response.setFailedRows(failedRows);
        return response;
    }

    // Returns null if valid, error message string if invalid
    private String validateSingleRecord(Map<String, Object> raw) {
        String recordType = String.valueOf(raw.getOrDefault("__recordType", "QUESTION"));
        if ("GROUP".equalsIgnoreCase(recordType)) {
            return validateSingleGroup(raw);
        }
        return validateSingleQuestion(raw);
    }

    private String validateSingleGroup(Map<String, Object> raw) {
        if (isBlank(raw, "groupType")) return "groupType is missing";
        if (isBlank(raw, "examType")) return "examType is missing";
        if (isBlank(raw, "subject")) return "subject is missing";

        try {
            GroupType.valueOf(String.valueOf(raw.get("groupType")).trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return "groupType '" + raw.get("groupType") + "' is invalid";
        }
        return null;
    }

    private String validateSingleQuestion(Map<String, Object> raw) {

        // Check required base fields
        if (isBlank(raw, "questionType"))
            return "questionType is missing";
        if (isBlank(raw, "questionText") && isBlank(raw, "problemStatement"))
            return "questionText or problemStatement is required";
        if (isBlank(raw, "examCategory"))
            return "examCategory is missing";
        if (isBlank(raw, "examType"))
            return "examType is missing";
        if (isBlank(raw, "subject"))
            return "subject is missing";
        if (isBlank(raw, "difficultyLevel"))
            return "difficultyLevel is missing";

        // Validate questionType is a known enum value
        String qType = normalizeQuestionType((String) raw.get("questionType"));
        raw.put("questionType", qType);
        try {
            QuestionType.valueOf(qType);
        } catch (Exception e) {
            return "questionType '" + raw.get("questionType") + "' is invalid";
        }

        // MCQ specific validation
        if (OPTION_BASED_TYPES.contains(qType)) {

            Object optionsObj = raw.get("options");
            if (optionsObj == null)
                return "options are required for " + qType;

            List<?> options = (List<?>) optionsObj;
            if (options.size() < 2)
                return "at least 2 options required for " + qType;

            boolean hasCorrect = options.stream()
                .anyMatch(o -> {
                    Map<?, ?> opt = (Map<?, ?>) o;
                    Object isCorrect = opt.get("isCorrect");
                    return Boolean.TRUE.equals(isCorrect)
                        || "true".equalsIgnoreCase(String.valueOf(isCorrect));
                });

            if (!hasCorrect)
                return "at least one option must be marked correct";
        }

        return null; // valid
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE — ROUTE AND SAVE
    // ═════════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    private void routeAndSave(Map<String, Object> raw, Map<String, Long> groupKeyToId) {
        String recordType = String.valueOf(raw.getOrDefault("__recordType", "QUESTION"));
        if ("GROUP".equalsIgnoreCase(recordType)) {
            saveGroup(raw, groupKeyToId);
            return;
        }

        raw.put("questionType", normalizeQuestionType((String) raw.get("questionType")));
        attachResolvedGroupId(raw, groupKeyToId);
        String qType = (String) raw.get("questionType");

        // Convert raw map to JSON string then to typed DTO
        // ObjectMapper handles the conversion cleanly
        switch (qType) {
            case "MCQ", "MULTI_CORRECT", "TRUE_FALSE",
                 "SYNONYM_ANTONYM", "ERROR_SPOTTING",
                 "READING_COMPREHENSION", "CLOZE_TEST", "TABLE_DI",
                 "BAR_CHART_DI", "PIE_CHART_DI", "LINE_GRAPH_DI", "CASELET_DI" -> {
                McqQuestionRequest req = objectMapper.convertValue(
                    raw, McqQuestionRequest.class);
                questionService.createMcqQuestion(req);
            }
            case "FILL_BLANK", "TEXT_ANSWER" -> {
                FillBlankRequest req = objectMapper.convertValue(
                    raw, FillBlankRequest.class);
                questionService.createFillBlank(req);
            }
            case "SENTENCE_ARRANGEMENT", "PARA_JUMBLE" -> {
                ArrangementRequest req = objectMapper.convertValue(
                    raw, ArrangementRequest.class);
                questionService.createArrangement(req);
            }
            case "MIRROR_IMAGE", "WATER_IMAGE", "PATTERN_MATRIX",
                 "ODD_ONE_OUT", "FIGURE_SERIES",
                 "IMAGE_MCQ", "IMAGE_OPTIONS" -> {
                ImageQuestionRequest req = objectMapper.convertValue(
                    raw, ImageQuestionRequest.class);
                questionService.createImageQuestion(req);
            }
            case "CODE_OUTPUT", "CODE_DEBUG",
                 "CODE_FILL", "SQL_OUTPUT", "FLOWCHART_MCQ" -> {
                CodeSnippetRequest req = objectMapper.convertValue(
                    raw, CodeSnippetRequest.class);
                questionService.createCodeSnippet(req);
            }
            case "CODE_WRITE" -> {
                CodingQuestionRequest req = objectMapper.convertValue(
                    raw, CodingQuestionRequest.class);
                questionService.createCodingQuestion(req);
            }
            case "EMAIL_WRITE", "ESSAY_WRITE",
                 "SPEECH_ROUND", "LISTENING_COMP" -> {
                WritingQuestionRequest req = objectMapper.convertValue(
                    raw, WritingQuestionRequest.class);
                questionService.createWritingQuestion(req);
            }
            default -> throw new IllegalArgumentException(
                "Unsupported questionType: " + qType);
        }
    }

    private void saveGroup(Map<String, Object> raw, Map<String, Long> groupKeyToId) {
        Map<String, Object> groupPayload = new LinkedHashMap<>(raw);
        String groupTypeRaw = String.valueOf(groupPayload.get("groupType"));
        groupPayload.put("groupType", groupTypeRaw.trim().toUpperCase(Locale.ROOT));
        QuestionGroupRequest groupReq = objectMapper.convertValue(groupPayload, QuestionGroupRequest.class);
        var response = questionService.createQuestionGroup(groupReq);
        Long groupId = response.getGroup() != null ? response.getGroup().getId() : null;
        if (groupId == null) {
            throw new IllegalStateException("Group created but groupId not returned");
        }
        String groupKey = resolveGroupKey(raw);
        if (groupKey != null && !groupKey.isBlank()) {
            groupKeyToId.put(groupKey, groupId);
        }
    }

    private void attachResolvedGroupId(Map<String, Object> raw, Map<String, Long> groupKeyToId) {
        if (raw.get("groupId") instanceof Number) return;
        Object ref = raw.get("groupKey");
        if (ref == null) ref = raw.get("groupRef");
        if (ref == null) ref = raw.get("groupAlias");
        if (ref == null) return;
        Long mappedId = groupKeyToId.get(String.valueOf(ref));
        if (mappedId != null) {
            raw.put("groupId", mappedId);
        }
    }

    private String resolveGroupKey(Map<String, Object> raw) {
        Object key = raw.get("groupKey");
        if (key == null) key = raw.get("groupRef");
        if (key == null) key = raw.get("groupAlias");
        if (key == null) key = raw.get("title");
        return key == null ? null : String.valueOf(key);
    }

    private List<Map<String, Object>> toMapList(Object value) {
        if (!(value instanceof List<?> list)) return List.of();
        return list.stream()
            .filter(item -> item instanceof Map)
            .map(item -> (Map<String, Object>) item)
            .toList();
    }

    private String normalizeQuestionType(String type) {
        if (type == null) return null;
        String normalized = type.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        return switch (normalized) {
            case "TEXT" -> "TEXT_ANSWER";
            case "MAIL_WRITE", "FORMAL_MAIL" -> "EMAIL_WRITE";
            default -> normalized;
        };
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE — UTILITY
    // ═════════════════════════════════════════════════════════════════

    private boolean isBlank(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val == null || val.toString().isBlank();
    }

    private String truncate(String text) {
        if (text == null) return "N/A";
        return text.length() > 60 ? text.substring(0, 60) + "..." : text;
    }
}