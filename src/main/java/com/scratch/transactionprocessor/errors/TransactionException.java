package com.scratch.transactionprocessor.errors;

import com.scratch.transactionprocessor.constants.ErrorCode;

public class TransactionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5128377255310480785L;
	
	private final ErrorCode errorCode;

	public TransactionException(ErrorCode code) {
		super();
		this.errorCode = code;
	}
	
	public TransactionException(String message, ErrorCode code) {
		super(message);
		this.errorCode = code;
	}
	
	public TransactionException(String message, Throwable cause, ErrorCode code) {
		super(message, cause);
		this.errorCode = code;
	}
	
	public TransactionException(Throwable cause, ErrorCode code) {
		super(cause);
		this.errorCode = code;
	}
	
	public ErrorCode getCode() {
		return this.errorCode;
	}
}