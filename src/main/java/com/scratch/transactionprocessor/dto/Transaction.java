package com.scratch.transactionprocessor.dto;

import java.util.Map;

import com.scratch.transactionprocessor.constants.Command;

public class Transaction {
	
	private Command cmd;

	public Transaction(Command cmd) {
		super();
		this.cmd = cmd;
	}

	public Command getCmd() {
		return cmd;
	}

	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}
	
	
}
