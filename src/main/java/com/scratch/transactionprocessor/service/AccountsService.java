package com.scratch.transactionprocessor.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.scratch.transactionprocessor.api.IAccountsService;
import com.scratch.transactionprocessor.constants.ErrorCode;
import com.scratch.transactionprocessor.dto.Account;
import com.scratch.transactionprocessor.errors.AccountNotExistsException;
import com.scratch.transactionprocessor.errors.TransactionException;

@Service
public class AccountsService implements IAccountsService {

	private Map<String, Account> accountsStore = new HashMap<String, Account>();
	
	
	@Override
	public void createAccount(String accountId) {
		Account account = new Account();
		account.setAccountId(accountId);
		account.setBalance(0);
		accountsStore.put(accountId, account);
	}

	@Override
	public double withdraw(String accountId, double amount) throws TransactionException {
		
		if (amount < 0) {
			throw new TransactionException(ErrorCode.INVALID_TRANSACTION);
		}
		
		// create account if not exists
		if (!accountsStore.containsKey(accountId)) {
			createAccount(accountId);
		}
		
		Account account = accountsStore.get(accountId);
		if (account.isFrozen()) {
			throw new TransactionException(ErrorCode.ACCOUNT_FROZEN);
		}
		
		double remainingBalance = account.getBalance() - amount;
		if (remainingBalance < 0) {
			throw new TransactionException(ErrorCode.LOW_BALANCE);
		} else {
			account.setBalance(remainingBalance);
		}
		
		accountsStore.put(accountId, account);
		return remainingBalance;
	}

	@Override
	public void deposit(String accountId, double amount) throws TransactionException {
		// create account if not exists
		if (amount < 0) {
			throw new TransactionException(ErrorCode.INVALID_TRANSACTION);
		}
		
		if (!accountsStore.containsKey(accountId)) {
			createAccount(accountId);
		}
		
		Account account = accountsStore.get(accountId);
		if (account.isFrozen()) {
			throw new TransactionException(ErrorCode.ACCOUNT_FROZEN);
		}
		account.setBalance(account.getBalance() + amount);
		accountsStore.put(accountId, account);
	}

	@Override
	public void freezeAccount(String accountId) {
		// create account if not exists
		if (!accountsStore.containsKey(accountId)) {
			createAccount(accountId);
		}
		
		Account account = accountsStore.get(accountId);
		account.setFrozen(true);
		accountsStore.put(accountId, account);
	}

	@Override
	public void thawAccount(String accountId) {
		// create account if not exists
		if (!accountsStore.containsKey(accountId)) {
			createAccount(accountId);
		}
		
		Account account = accountsStore.get(accountId);
		account.setFrozen(false);
		accountsStore.put(accountId, account);
	}

	@Override
	public double getAccountBalance(String accountId) throws AccountNotExistsException {
		if (!accountsStore.containsKey(accountId)) {
			throw new AccountNotExistsException(ErrorCode.ACCOUNT_NOT_EXISTS);
		}
		
		Account account = accountsStore.get(accountId);
		return account.getBalance();
	}

	@Override
	public boolean isActive(String accountId) throws AccountNotExistsException {
		if (!accountsStore.containsKey(accountId)) {
			throw new AccountNotExistsException(ErrorCode.ACCOUNT_NOT_EXISTS);
		}
		
		Account account = accountsStore.get(accountId);
		return !account.isFrozen();
	}

}
