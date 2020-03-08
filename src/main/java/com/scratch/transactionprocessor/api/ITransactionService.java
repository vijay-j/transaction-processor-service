package com.scratch.transactionprocessor.api;

import java.util.List;

import com.scratch.transactionprocessor.dto.Transaction;

public interface ITransactionService {
	/**
	 * Takes batch of transactions and execute them
	 * returns failed transactions
	 * @param transactions
	 * @return
	 */
	public List<Transaction> executeTransactions(List<Transaction> transactions);

}
