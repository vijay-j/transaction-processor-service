package com.scratch.transactionprocessor.api;

import com.scratch.transactionprocessor.errors.AccountNotExistsException;
import com.scratch.transactionprocessor.errors.TransactionException;

public interface IAccountsService {
	/**
	 * Create new account with zero balance
	 * @param accountId
	 * @return
	 */
	public void createAccount(String accountId);
	
	
	/**
	 * Get account balance
	 * @param accountId
	 * @return
	 */
	public double getAccountBalance(String accountId) throws AccountNotExistsException;
	
	/***
	 * Check if account is active or not
	 */
	public boolean isActive(String accountId) throws AccountNotExistsException;
	
	/**
	 * Withdraw specific amount from account
	 * @param accountId
	 * @return
	 */
	public double withdraw(String accountId, double amount) throws TransactionException;
	
	/**
	 * Deposit specific amount from account
	 * @param accountId
	 * @param amount
	 */
	public void deposit(String accountId, double amount) throws TransactionException;
	
	/**
	 * Freeze account
	 * @param accountId
	 * @return
	 */
	public void freezeAccount(String accountId) throws TransactionException;
	
	/**
	 * Thaw Account
	 * @param accountId
	 * @return
	 */
	public void thawAccount(String accountId) throws TransactionException;
	
}
