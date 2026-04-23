package com.BrainBlitz.dto.request;


import java.util.List;

public class CheckAnswerRequest {

 private Long questionId;

 // For MCQ / TRUE_FALSE / SYNONYM_ANTONYM / CODE_OUTPUT etc.
 // Single selected option id
 private Long selectedOptionId;

 // For MULTI_CORRECT — multiple selected option ids
 private List<Long> selectedOptionIds;

 // For FILL_BLANK — typed answer
 private String typedAnswer;

 // For SENTENCE_ARRANGEMENT / PARA_JUMBLE — ordered list of items
 private String arrangedOrder;
 

 // Getters & Setters
 public Long getQuestionId() { return questionId; }
 public void setQuestionId(Long questionId) { this.questionId = questionId; }

 public Long getSelectedOptionId() { return selectedOptionId; }
 public void setSelectedOptionId(Long selectedOptionId) { this.selectedOptionId = selectedOptionId; }

 public List<Long> getSelectedOptionIds() { return selectedOptionIds; }
 public void setSelectedOptionIds(List<Long> selectedOptionIds) { this.selectedOptionIds = selectedOptionIds; }

 public String getTypedAnswer() { return typedAnswer; }
 public void setTypedAnswer(String typedAnswer) { this.typedAnswer = typedAnswer; }

 public String getArrangedOrder() { return arrangedOrder; }
 public void setArrangedOrder(String arrangedOrder) { this.arrangedOrder = arrangedOrder; }
}
