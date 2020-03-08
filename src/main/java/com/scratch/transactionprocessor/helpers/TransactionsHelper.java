package com.scratch.transactionprocessor.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scratch.transactionprocessor.constants.Command;
import com.scratch.transactionprocessor.dto.DepositTransaction;
import com.scratch.transactionprocessor.dto.FreezeTransaction;
import com.scratch.transactionprocessor.dto.ThawTransaction;
import com.scratch.transactionprocessor.dto.Transaction;
import com.scratch.transactionprocessor.dto.TransferTransaction;
import com.scratch.transactionprocessor.dto.WithdrawTransaction;

public class TransactionsHelper {
	private static final Logger logger = LoggerFactory.getLogger(TransactionsHelper.class);
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static List<Map<String, Object>> checkTransactionValidity(List<Map<String, Object>> rawTransactions) {
		List<Map<String, Object>> invalidTransactions = new ArrayList<>();
		List<Integer> invalidIndexes = new ArrayList<>();
		
		for (Map<String, Object> transaction: rawTransactions) {
			if (!isValid(transaction)) {
				logger.error("Invalid Transaction command !!");
				invalidTransactions.add(transaction);
				invalidIndexes.add(rawTransactions.indexOf(transaction));
			}
		}
		
		if (invalidIndexes.size() > 0) {
			for (Integer index: invalidIndexes) {
				rawTransactions.remove(index.intValue());
			}			
		}
		
		return invalidTransactions;
	}
	
	public static List<Transaction> getTransactionsList(@Valid List<Map<String, Object>> rawTransactions) {
		List<Transaction> transactions = new ArrayList<>();
		rawTransactions.forEach(transaction -> {
			String cmd = (String) transaction.get("cmd");
			Transaction transactionObj = null;
			switch(cmd) {
				case "DEPOSIT":
					transactionObj = mapper.convertValue(transaction, DepositTransaction.class);
					break;
				case "WITHDRAW":
					transactionObj = mapper.convertValue(transaction, WithdrawTransaction.class);
					break;
				case "FREEZE":
					transactionObj = mapper.convertValue(transaction, FreezeTransaction.class);
					break;
				case "THAW":
					transactionObj = mapper.convertValue(transaction, ThawTransaction.class);
					break;
				case "XFER":
					transactionObj = mapper.convertValue(transaction, TransferTransaction.class);
					break;
				default:
					break;
			}
			transactions.add(transactionObj);
		});
		
		return transactions;
	}

	public static List<Map<String, Object>> convertToRawTransactions(@Valid List<Transaction> transactions) {
		List<Map<String, Object>> rawTransactions = new ArrayList<>();
		transactions.forEach(transaction -> {
			Map<String, Object> transactionObj = mapper.convertValue(transaction, Map.class);
			rawTransactions.add(transactionObj);
		});
		
		return rawTransactions;
	}
	
	private static boolean isValid(Map<String, Object> transaction) {
		Command[] values = Command.values();
		String cmd = (String) transaction.get("cmd");
		
		for(Command type: values) {
			if (type.toString().equals(cmd)) {
				return isAllRequiredAttributesPresent(cmd, transaction);
			}
		}
		return false;
	}

	private static boolean isAllRequiredAttributesPresent(String cmd, Map<String, Object> transaction) {
		
		switch(cmd) {
			case "DEPOSIT":
			case "WITHDRAW":
				return transaction.get("accountId") != null && transaction.get("amount") != null;
			case "FREEZE":
			case "THAW":
				return transaction.get("accountId") != null;
			case "XFER":
				return transaction.get("fromId") != null && transaction.get("toId") != null
					&& transaction.get("amount") != null;
		}
		return false;
	}
}
