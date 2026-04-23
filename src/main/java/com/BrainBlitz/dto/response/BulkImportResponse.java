// src/main/java/com/BrainBlitz/dto/response/BulkImportResponse.java

package com.BrainBlitz.dto.response;

import lombok.*;
import java.util.List;


public class BulkImportResponse {

    private Integer totalSubmitted;
    private Integer successCount;
    private Integer failedCount;
    private Boolean isValidationOnly;       // true = validate step, false = confirm step
    private List<FailedRow> failedRows;
    
    public BulkImportResponse(Integer totalSubmitted, Integer successCount, Integer failedCount,
			Boolean isValidationOnly, List<FailedRow> failedRows) {
		super();
		this.totalSubmitted = totalSubmitted;
		this.successCount = successCount;
		this.failedCount = failedCount;
		this.isValidationOnly = isValidationOnly;
		this.failedRows = failedRows;
	}
    
	public BulkImportResponse() {
		super();
	}
	
	public Integer getTotalSubmitted() {
		return totalSubmitted;
	}


	public void setTotalSubmitted(Integer totalSubmitted) {
		this.totalSubmitted = totalSubmitted;
	}


	public Integer getSuccessCount() {
		return successCount;
	}


	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}


	public Integer getFailedCount() {
		return failedCount;
	}


	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}


	public Boolean getIsValidationOnly() {
		return isValidationOnly;
	}


	public void setIsValidationOnly(Boolean isValidationOnly) {
		this.isValidationOnly = isValidationOnly;
	}


	public List<FailedRow> getFailedRows() {
		return failedRows;
	}


	public void setFailedRows(List<FailedRow> failedRows) {
		this.failedRows = failedRows;
	}

    public static class FailedRow {
        private Integer rowNumber;
        private String questionText;        // first 60 chars for identification
        private String reason;  
        // e.g. "questionType is missing"
		public FailedRow(Integer rowNumber, String questionText, String reason) {
			super();
			this.rowNumber = rowNumber;
			this.questionText = questionText;
			this.reason = reason;
		}
		public FailedRow() {
			super();
		}
		public Integer getRowNumber() {
			return rowNumber;
		}
		public void setRowNumber(Integer rowNumber) {
			this.rowNumber = rowNumber;
		}
		public String getQuestionText() {
			return questionText;
		}
		public void setQuestionText(String questionText) {
			this.questionText = questionText;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
        
        
    }
}