// src/main/java/com/BrainBlitz/service/impl/TestCaseServiceImpl.java

package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.request.SolutionStepRequest;
import com.BrainBlitz.dto.request.TestCaseRequest;
import com.BrainBlitz.dto.response.QuestionResponse;
import com.BrainBlitz.entity.CodingQuestion;
import com.BrainBlitz.entity.SolutionStep;
import com.BrainBlitz.entity.TestCase;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.CodingQuestionRepository;
import com.BrainBlitz.repository.SolutionStepRepository;
import com.BrainBlitz.repository.TestCaseRepository;
import com.BrainBlitz.service.QuestionService;
import com.BrainBlitz.service.TestCaseService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestCaseServiceImpl implements TestCaseService {

	
    private  TestCaseRepository testCaseRepository;
    private  SolutionStepRepository solutionStepRepository;
    private  CodingQuestionRepository codingQuestionRepository;
    private  QuestionService questionService;


    // ═════════════════════════════════════════════════════════════════
    // TEST CASE METHODS
    // ═════════════════════════════════════════════════════════════════

    
    @Override
    public QuestionResponse addTestCases(Long questionId,
                                          List<TestCaseRequest> requests) {

        CodingQuestion codingQuestion = getCodingQuestionByQuestionId(questionId);

        List<TestCase> testCases = requests.stream()
            .map(req -> buildTestCase(req, codingQuestion))
            .collect(Collectors.toList());

        testCaseRepository.saveAll(testCases);

        return questionService.getQuestionById(questionId);
    }

    public TestCaseServiceImpl() {
		super();
	}

	public TestCaseServiceImpl(TestCaseRepository testCaseRepository, SolutionStepRepository solutionStepRepository,
			CodingQuestionRepository codingQuestionRepository, QuestionService questionService) {
		super();
		this.testCaseRepository = testCaseRepository;
		this.solutionStepRepository = solutionStepRepository;
		this.codingQuestionRepository = codingQuestionRepository;
		this.questionService = questionService;
	}

	// Bulk import — same as addTestCases but
    // validates no duplicate inputs before saving
    @Override
    public QuestionResponse bulkImportTestCases(Long questionId,
                                                  List<TestCaseRequest> requests) {

        CodingQuestion codingQuestion = getCodingQuestionByQuestionId(questionId);

        // Fetch existing inputs to prevent duplicates
        List<String> existingInputs = testCaseRepository
            .findByCodingQuestionId(codingQuestion.getId())
            .stream()
            .map(TestCase::getInput)
            .collect(Collectors.toList());

        List<TestCase> newTestCases = requests.stream()
            .filter(req -> !existingInputs.contains(req.getInput()))
            .map(req -> buildTestCase(req, codingQuestion))
            .collect(Collectors.toList());

        if (newTestCases.isEmpty()) {
            throw new IllegalArgumentException(
                "All provided test cases already exist for this question");
        }

        testCaseRepository.saveAll(newTestCases);

        return questionService.getQuestionById(questionId);
    }

    @Override
    public QuestionResponse updateTestCase(Long questionId,
                                            Long testCaseId,
                                            TestCaseRequest request) {

        // Verify coding question exists
        getCodingQuestionByQuestionId(questionId);

        TestCase testCase = testCaseRepository.findById(testCaseId)
            .orElseThrow(() -> new ResourceNotFoundException("TestCase", testCaseId));

        // Update only provided fields
        if (request.getInput() != null)
            testCase.setInput(request.getInput());
        if (request.getExpectedOutput() != null)
            testCase.setExpectedOutput(request.getExpectedOutput());
        if (request.getIsSample() != null)
            testCase.setIsSample(request.getIsSample());
        if (request.getIsHidden() != null)
            testCase.setIsHidden(request.getIsHidden());
        if (request.getExplanation() != null)
            testCase.setExplanation(request.getExplanation());
        if (request.getWeightage() != null)
            testCase.setWeightage(request.getWeightage());

        testCaseRepository.save(testCase);

        return questionService.getQuestionById(questionId);
    }

    @Override
    public void deleteTestCase(Long questionId, Long testCaseId) {

        // Verify coding question exists
        getCodingQuestionByQuestionId(questionId);

        if (!testCaseRepository.existsById(testCaseId))
            throw new ResourceNotFoundException("TestCase", testCaseId);

        testCaseRepository.deleteById(testCaseId);
    }


    // ═════════════════════════════════════════════════════════════════
    // SOLUTION STEP METHODS
    // ═════════════════════════════════════════════════════════════════

    @Override
    public QuestionResponse addSolutionSteps(Long questionId,
                                              List<SolutionStepRequest> requests) {

        CodingQuestion codingQuestion = getCodingQuestionByQuestionId(questionId);

        // Get current max step number to continue from
        List<SolutionStep> existing = solutionStepRepository
            .findByCodingQuestionIdOrderByStepNumber(codingQuestion.getId());

        int startFrom = existing.isEmpty() ? 1
            : existing.get(existing.size() - 1).getStepNumber() + 1;

        List<SolutionStep> steps = requests.stream()
            .map(req -> {
                // Use provided stepNumber or auto-assign
                int stepNum = req.getStepNumber() != null
                    ? req.getStepNumber()
                    : startFrom;

                return buildSolutionStep(req, codingQuestion, stepNum);
            })
            .collect(Collectors.toList());

        solutionStepRepository.saveAll(steps);

        return questionService.getQuestionById(questionId);
    }

    @Override
    public QuestionResponse updateSolutionStep(Long questionId,
                                                Long stepId,
                                                SolutionStepRequest request) {

        // Verify coding question exists
        getCodingQuestionByQuestionId(questionId);

        SolutionStep step = solutionStepRepository.findById(stepId)
            .orElseThrow(() -> new ResourceNotFoundException("SolutionStep", stepId));

        // Update only provided fields
        if (request.getStepNumber() != null)
            step.setStepNumber(request.getStepNumber());
        if (request.getStepTitle() != null)
            step.setStepTitle(request.getStepTitle());
        if (request.getStepDescription() != null)
            step.setStepDescription(request.getStepDescription());
        if (request.getCodeAtThisStep() != null)
            step.setCodeAtThisStep(request.getCodeAtThisStep());
        if (request.getImageUrl() != null)
            step.setImageUrl(request.getImageUrl());

        solutionStepRepository.save(step);

        return questionService.getQuestionById(questionId);
    }

    @Override
    public void deleteSolutionStep(Long questionId, Long stepId) {

        // Verify coding question exists
        getCodingQuestionByQuestionId(questionId);

        if (!solutionStepRepository.existsById(stepId))
            throw new ResourceNotFoundException("SolutionStep", stepId);

        solutionStepRepository.deleteById(stepId);
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE HELPER METHODS
    // ═════════════════════════════════════════════════════════════════

    // Fetch CodingQuestion by the parent questionId
    private CodingQuestion getCodingQuestionByQuestionId(Long questionId) {
        return codingQuestionRepository.findByQuestionId(questionId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "CodingQuestion not found for questionId: " + questionId));
    }

    private TestCase buildTestCase(TestCaseRequest req,
            CodingQuestion codingQuestion) {
		TestCase tc = new TestCase();
		tc.setCodingQuestion(codingQuestion);
		tc.setInput(req.getInput());
		tc.setExpectedOutput(req.getExpectedOutput());
		tc.setIsSample(req.getIsSample() != null ? req.getIsSample() : false);
		tc.setIsHidden(req.getIsHidden() != null ? req.getIsHidden() : false);		tc.setExplanation(req.getExplanation());
		tc.setWeightage(req.getWeightage() != null ? req.getWeightage() : 1);
		return tc;
		}

    private SolutionStep buildSolutionStep(SolutionStepRequest req,
            CodingQuestion codingQuestion,
		            int stepNumber) {
		SolutionStep step = new SolutionStep();
		step.setCodingQuestion(codingQuestion);
		step.setStepNumber(stepNumber);
		step.setStepTitle(req.getStepTitle());
		step.setStepDescription(req.getStepDescription());
		step.setCodeAtThisStep(req.getCodeAtThisStep());
		step.setImageUrl(req.getImageUrl());
		return step;
		}
}
