package org.freelesson.sendsms.exception;

import java.util.Map;
import java.util.Map.Entry;

public final class ErrorResponse {

	private int status;
	private Map<String, Map<String, String>> errors;
	private String message;

	public ErrorResponse(int status, String message) {
		this.setStatus(status);
		this.setMessage(message);
	}

	public ErrorResponse(int status, String message, ErrorHandler errorHandler) {
		this(status, message);
		this.errors = errorHandler.getErrors();
	}

	public ErrorResponse(int status, String message, Map<String, String> errorMap) {
		this(status, message);
		ErrorHandler eh = new ErrorHandler();
		if (errorMap != null) {
			for (Entry<String, String> error : errorMap.entrySet()) {
				eh.addGeneralError(error.getKey(), error.getValue());
			}
		}
		this.errors = eh.getErrors();
	}

	public int getStatus() {
		return status;
	}

	void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Map<String, String>> getErrors() {
		return errors;
	}

}
