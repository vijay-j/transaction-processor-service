package com.scratch.transactionprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.scratch.transactionprocessor.constants.Command;
import com.scratch.transactionprocessor.dto.DepositTransaction;
import com.scratch.transactionprocessor.dto.FreezeTransaction;
import com.scratch.transactionprocessor.dto.ThawTransaction;
import com.scratch.transactionprocessor.dto.Transaction;
import com.scratch.transactionprocessor.dto.TransferTransaction;
import com.scratch.transactionprocessor.dto.WithdrawTransaction;
import com.scratch.transactionprocessor.helpers.TransactionsHelper;

@SpringBootTest
public class TransactionsHelperTest {
	
	@Test
	public void testShouldReturnValidTransaction() {
		List<Map<String, Object>> rawTransactions = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("cmd", "DEPOSIT");
		map.put("accountId", "ACT300");
		map.put("amount", 100.00);
		rawTransactions.add(map);
		List<Map<String, Object>> invalidTx = TransactionsHelper.checkTransactionValidity(rawTransactions);
		assertEquals(0, invalidTx.size());
	}
	
	@Test
	public void testShouldReturnInValidTransaction() {
		List<Map<String, Object>> rawTransactions = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("cmd", "TEST");
		map.put("accountId", "ACT300");
		map.put("amount", 100.00);
		rawTransactions.add(map);
		List<Map<String, Object>> invalidTx = TransactionsHelper.checkTransactionValidity(rawTransactions);
		assertEquals(1, invalidTx.size());
	}
	
	@Test
	public void testValidateDepositObject() {
		List<Map<String, Object>> rawTransactions = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("cmd", "DEPOSIT");
		map.put("accountId", "ACT300");
		map.put("amount", 100.00);
		rawTransactions.add(map);
		List<Transaction> transactionsList = TransactionsHelper.getTransactionsList(rawTransactions);
		
		DepositTransaction transaction = (DepositTransaction) transactionsList.get(0);
		
		assertEquals(transaction.getAccountId(), "ACT300");
		assertEquals(transaction.getAmount(), 100);
		assertEquals(transaction.getCmd(), Command.DEPOSIT);
	}
	
	@Test
	public void testValidateWithdrawObject() {
		List<Map<String, Object>> rawTransactions = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("cmd", "WITHDRAW");
		map.put("accountId", "ACT300");
		map.put("amount", 100.00);
		rawTransactions.add(map);
		List<Transaction> transactionsList = TransactionsHelper.getTransactionsList(rawTransactions);
		
		WithdrawTransaction transaction = (WithdrawTransaction) transactionsList.get(0);
		
		assertEquals(transaction.getAccountId(), "ACT300");
		assertEquals(transaction.getAmount(), 100);
		assertEquals(transaction.getCmd(), Command.WITHDRAW);
	}
	
	@Test
	public void testValidateTransferObject() {
		List<Map<String, Object>> rawTransactions = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("cmd", "XFER");
		map.put("toId", "ACT300");
		map.put("fromId", "ACT100");
		map.put("amount", 100.00);
		rawTransactions.add(map);
		List<Transaction> transactionsList = TransactionsHelper.getTransactionsList(rawTransactions);
		
		TransferTransaction transaction = (TransferTransaction) transactionsList.get(0);
		
		assertEquals(transaction.getToId(), "ACT300");
		assertEquals(transaction.getFromId(), "ACT100");
		assertEquals(transaction.getAmount(), 100);
		assertEquals(transaction.getCmd(), Command.XFER);
	}
	
	@Test
	public void testValidateFreezeObject() {
		List<Map<String, Object>> rawTransactions = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("cmd", "FREEZE");
		map.put("accountId", "ACT300");
		rawTransactions.add(map);
		List<Transaction> transactionsList = TransactionsHelper.getTransactionsList(rawTransactions);
		
		FreezeTransaction transaction = (FreezeTransaction) transactionsList.get(0);
		
		assertEquals(transaction.getAccountId(), "ACT300");
		assertEquals(transaction.getCmd(), Command.FREEZE);
	}
	
	@Test
	public void testValidateThawObject() {
		List<Map<String, Object>> rawTransactions = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("cmd", "THAW");
		map.put("accountId", "ACT300");
		rawTransactions.add(map);
		List<Transaction> transactionsList = TransactionsHelper.getTransactionsList(rawTransactions);
		
		ThawTransaction transaction = (ThawTransaction) transactionsList.get(0);
		
		assertEquals(transaction.getAccountId(), "ACT300");
		assertEquals(transaction.getCmd(), Command.THAW);
	}
}
