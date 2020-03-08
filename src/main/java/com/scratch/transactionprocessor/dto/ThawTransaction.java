package com.scratch.transactionprocessor.dto;

import com.scratch.transactionprocessor.constants.Command;

public class ThawTransaction extends Transaction {
	public ThawTransaction() {
		super(Command.THAW);
	}
	
	private String accountId;
	
	public String getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
}
