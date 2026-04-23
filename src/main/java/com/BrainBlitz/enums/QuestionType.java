package com.BrainBlitz.enums;

public enum QuestionType {

    // ── Text based ──────────────────────────────
    MCQ,                  // Single correct MCQ
    MULTI_CORRECT,        // Multiple correct MCQ
    TRUE_FALSE,           // True or False
    FILL_BLANK,           // Fill in the blank
    TEXT_ANSWER,          // Open text answer

    // ── Arrangement ─────────────────────────────
    SENTENCE_ARRANGEMENT, // Arrange sentences P,Q,R,S
    PARA_JUMBLE,          // Rearrange paragraphs

    // ── Vocabulary ──────────────────────────────
    SYNONYM_ANTONYM,      // Vocabulary MCQ
    ERROR_SPOTTING,       // Find grammatical error

    // ── Image based ─────────────────────────────
    MIRROR_IMAGE,         // Mirror image question
    WATER_IMAGE,          // Water reflection question
    IMAGE_MCQ,            // Image question + text options
    IMAGE_OPTIONS,        // Text question + image options
    PATTERN_MATRIX,       // 3x3 pattern matrix
    ODD_ONE_OUT,          // Pick odd image
    FIGURE_SERIES,        // Figure sequence

    // ── Group based (passage / DI) ───────────────
    READING_COMPREHENSION,// Passage + MCQ questions
    CLOZE_TEST,           // Passage with blanks
    TABLE_DI,             // Table data + MCQ
    BAR_CHART_DI,         // Bar chart + MCQ
    PIE_CHART_DI,         // Pie chart + MCQ
    LINE_GRAPH_DI,        // Line graph + MCQ
    CASELET_DI,           // Text data passage + MCQ

    // ── Coding ──────────────────────────────────
    CODE_OUTPUT,          // Predict code output (MCQ)
    CODE_WRITE,           // Write code solution
    CODE_DEBUG,           // Fix buggy code
    CODE_FILL,            // Fill missing code line
    SQL_OUTPUT,           // Predict SQL output (MCQ)
    FLOWCHART_MCQ,        // Flowchart image + MCQ

    // ── Writing / Speech ────────────────────────
    EMAIL_WRITE,          // Write formal/informal email
    ESSAY_WRITE,          // Write essay
    SPEECH_ROUND,         // Record speech
    LISTENING_COMP        // Listen audio + MCQ
}