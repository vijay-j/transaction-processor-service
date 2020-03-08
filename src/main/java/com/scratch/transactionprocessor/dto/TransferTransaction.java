package com.scratch.transactionprocessor.dto;

import com.scratch.transactionprocessor.constants.Command;

public class TransferTransaction extends Transaction {

	public TransferTransaction() {
		super(Command.XFER);
	}
	
	private String fromId;
	private String toId;
	private double amount;
	

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	
}
