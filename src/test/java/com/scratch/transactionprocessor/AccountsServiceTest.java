package com.scratch.transactionprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scratch.transactionprocessor.constants.ErrorCode;
import com.scratch.transactionprocessor.errors.AccountNotExistsException;
import com.scratch.transactionprocessor.errors.TransactionException;
import com.scratch.transactionprocessor.service.AccountsService;

@SpringBootTest
public class AccountsServiceTest {

	@Autowired
	AccountsService accountsService;
	
	@Test
	public void testCreateAccount() {
		String accountId = "ACT100";
		accountsService.createAccount(accountId);
		try {
			assertEquals(accountsService.getAccountBalance(accountId), 0);
			assertTrue(accountsService.isActive(accountId));
		} catch (AccountNotExistsException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDepositSuccess() {
		
		double amount = 100;
		String accountId = "ACT100";
		try {
			accountsService.deposit(accountId, amount);
			assertEquals(accountsService.getAccountBalance(accountId), 100);
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccountNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWithdrawSuccess() {
		
		double amount = 100;
		String accountId = "ACT100";
		try {
			accountsService.deposit(accountId, amount);
			accountsService.withdraw(accountId, amount);
			assertEquals(accountsService.getAccountBalance(accountId), 0);
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccountNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFreezeSucces() {
		
		String accountId = "ACT100";
		try {
			accountsService.createAccount(accountId);
			accountsService.freezeAccount(accountId);
			assertFalse(accountsService.isActive(accountId));
		} catch (AccountNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testThawSucces() {
		
		String accountId = "ACT100";
		try {
			accountsService.createAccount(accountId);
			accountsService.freezeAccount(accountId);
			accountsService.thawAccount(accountId);
			assertTrue(accountsService.isActive(accountId));
		} catch (AccountNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDepositFail() {
		
		double amount = 100;
		String accountId = "ACT100";
		TransactionException thrown = assertThrows(TransactionException.class, () -> {
			accountsService.createAccount(accountId);
			accountsService.freezeAccount(accountId);
			accountsService.deposit(accountId, amount);
		});
		
		assertEquals(ErrorCode.ACCOUNT_FROZEN, thrown.getCode());
	}
	
	@Test
	public void testWithdrawFreezeFail() {
		
		double amount = 100;
		String accountId = "ACT100";
		TransactionException thrown = assertThrows(TransactionException.class, () -> {
			accountsService.createAccount(accountId);
			accountsService.freezeAccount(accountId);
			accountsService.withdraw(accountId, amount);
		});
		
		assertEquals(ErrorCode.ACCOUNT_FROZEN, thrown.getCode());
	}
	
	@Test
	public void testWithdrawFail() {
		
		double amount = 100;
		String accountId = "ACT100";
		TransactionException thrown = assertThrows(TransactionException.class, () -> {
			accountsService.createAccount(accountId);
			accountsService.withdraw(accountId, amount);
		});
		
		assertEquals(ErrorCode.LOW_BALANCE, thrown.getCode());
	}
}
