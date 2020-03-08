package com.scratch.transactionprocessor.dto;

import com.scratch.transactionprocessor.constants.Command;

public class FreezeTransaction extends Transaction {
	public FreezeTransaction() {
		super(Command.FREEZE);
	}
	
	private String accountId;
	
	public String getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
}
