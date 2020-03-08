package com.scratch.transactionprocessor.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scratch.transactionprocessor.dto.Transaction;
import com.scratch.transactionprocessor.helpers.TransactionsHelper;
import com.scratch.transactionprocessor.service.TransactionService;


@RestController
public class TransactionController {

	private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	TransactionService transactionService;
	
	@PostMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> createEmployee(@Valid @RequestBody List<Map<String, Object>> transactions) {
		
		logger.info("Got transactions of size " + transactions.size());
		
		List<Map<String, Object>> rawFailedTransactions = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> invalidTransactions = TransactionsHelper.checkTransactionValidity(transactions);
		
		if (invalidTransactions != null && invalidTransactions.size() > 0) {
			rawFailedTransactions.addAll(invalidTransactions);
		}
		
		List<Transaction> transactionsList = TransactionsHelper.getTransactionsList(transactions);
		List<Transaction> failedTransactions = transactionService.executeTransactions(transactionsList);
		
		if (failedTransactions != null && failedTransactions.size() > 0) {
			logger.info("Failed transactions of size " + failedTransactions.size());
			rawFailedTransactions.addAll(TransactionsHelper.convertToRawTransactions(failedTransactions));			
		}
		
		return rawFailedTransactions;
	}
}
