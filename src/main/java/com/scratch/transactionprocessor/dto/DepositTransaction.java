package com.scratch.transactionprocessor.dto;

import com.scratch.transactionprocessor.constants.Command;

public class DepositTransaction extends Transaction {
	
	public DepositTransaction() {
		super(Command.DEPOSIT);
	}
	
	private String accountId;
	private double amount;
	
	public String getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
