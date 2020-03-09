package com.scratch.transactionprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scratch.transactionprocessor.dto.DepositTransaction;
import com.scratch.transactionprocessor.dto.FreezeTransaction;
import com.scratch.transactionprocessor.dto.Transaction;
import com.scratch.transactionprocessor.dto.TransferTransaction;
import com.scratch.transactionprocessor.errors.AccountNotExistsException;
import com.scratch.transactionprocessor.service.AccountsService;
import com.scratch.transactionprocessor.service.TransactionService;

@SpringBootTest
public class TransactionServiceTest {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountsService accountsService;
	
	@Test
	public void testTransferSuccess() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		DepositTransaction depostTx = new DepositTransaction();
		String accountId = "ACT57";
		depostTx.setAccountId(accountId);
		depostTx.setAmount(100);
		transactions.add(depostTx);
		
		TransferTransaction tx = new TransferTransaction();
		tx.setAmount(100);
		tx.setFromId(accountId);
		
		String toId = "ACT99";
		tx.setToId(toId);
		transactions.add(tx);
		List<Transaction> failedTx = transactionService.executeTransactions(transactions);
		System.out.println("FAILEDDDDD  " + failedTx.size());
		assertEquals(0, failedTx.size());
	}
	
	@Test
	public void testTransferFailed() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		DepositTransaction depostTx = new DepositTransaction();
		String accountId = "ACT58";
		depostTx.setAccountId(accountId);
		depostTx.setAmount(100);
		transactions.add(depostTx);
		
		TransferTransaction tx = new TransferTransaction();
		tx.setAmount(1000);
		tx.setFromId(accountId);
		
		String toId = "ACT103";
		tx.setToId(toId);
		transactions.add(tx);
		List<Transaction> failedTx = transactionService.executeTransactions(transactions);
		
	
		assertEquals(1, failedTx.size());
	}
	
	@Test
	public void testTransferRollbackSuccess() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		DepositTransaction depostTx = new DepositTransaction();
		String accountId = "ACT58";
		depostTx.setAccountId(accountId);
		depostTx.setAmount(100);
		transactions.add(depostTx);
		
		String toId = "ACT103";
		FreezeTransaction fzTx = new FreezeTransaction();
		fzTx.setAccountId(toId);
		transactions.add(fzTx);
		
		TransferTransaction tx = new TransferTransaction();
		tx.setAmount(100);
		tx.setFromId(accountId);
		tx.setToId(toId);
		transactions.add(tx);
		List<Transaction> failedTx = transactionService.executeTransactions(transactions);
		
		assertEquals(1, failedTx.size());
		
		try {
			assertEquals(100, accountsService.getAccountBalance(accountId));
		} catch (AccountNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
