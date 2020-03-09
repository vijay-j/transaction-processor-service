package com.scratch.transactionprocessor.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scratch.transactionprocessor.api.ITransactionService;
import com.scratch.transactionprocessor.constants.ErrorCode;
import com.scratch.transactionprocessor.dto.DepositTransaction;
import com.scratch.transactionprocessor.dto.FreezeTransaction;
import com.scratch.transactionprocessor.dto.ThawTransaction;
import com.scratch.transactionprocessor.dto.Transaction;
import com.scratch.transactionprocessor.dto.TransferTransaction;
import com.scratch.transactionprocessor.dto.WithdrawTransaction;
import com.scratch.transactionprocessor.errors.TransactionException;

@Service
public class TransactionService implements ITransactionService {

	@Autowired
	private AccountsService accountsService;
	
	private final Logger logger = LoggerFactory.getLogger(TransactionService.class);
	@Override
	public List<Transaction> executeTransactions(List<Transaction> transactions) {
		
		List<Transaction> failedTransactions = new ArrayList<Transaction>();
		transactions.forEach(transaction -> {
			logger.info("Executing transaction: {} ", transaction.getCmd().toString());
			try {
				switch (transaction.getCmd()) {
				case DEPOSIT:
					DepositTransaction depositTx = (DepositTransaction) transaction;
					executeDepositForAccount(depositTx);	
					break;
				case WITHDRAW:
					WithdrawTransaction withdrawTx = (WithdrawTransaction) transaction;
					executeWithdrawForAccount(withdrawTx);
					break;
				case FREEZE:
					FreezeTransaction freezeTx = (FreezeTransaction) transaction;
					executeFreezeForAccount(freezeTx);	
					break;
				case THAW:
					ThawTransaction thawTx = (ThawTransaction) transaction;
					executeThawForAccount(thawTx);
					break;
				case XFER:
					TransferTransaction transferTx = (TransferTransaction) transaction;
					executeTransferTransaction(transferTx);
					break;
				default:
					// throw exception
					break;
				}
			} catch(TransactionException ex) {
				logger.info("Failed transaction: {} ", transaction.getCmd().toString());
				failedTransactions.add(transaction);
			}
		});
		
		return failedTransactions;
	}

	private void executeTransferTransaction(TransferTransaction transferTx) throws TransactionException {
		
		String toAccountId = transferTx.getToId();
		double amount = transferTx.getAmount();
		String fromAccountId = transferTx.getFromId();
		
		accountsService.withdraw(fromAccountId, amount);
		
		// If fromAccountId is frozen or transaction failed it will not come here
		try {
			accountsService.deposit(toAccountId, amount);
		} catch (TransactionException e) {
			// If this fails and the above one succeeds, deposit the amount back to fromAccountId
			accountsService.deposit(fromAccountId, amount);
			throw new TransactionException(ErrorCode.ACCOUNT_FROZEN);
		}
	}

	private void executeThawForAccount(ThawTransaction thawTx) {
		String accountId = thawTx.getAccountId();
		accountsService.thawAccount(accountId);
	}

	private void executeFreezeForAccount(FreezeTransaction freezeTx) {
		String accountId = freezeTx.getAccountId();
		accountsService.freezeAccount(accountId);
	}

	private void executeWithdrawForAccount(WithdrawTransaction withdrawTx) throws TransactionException {
		String accountId = withdrawTx.getAccountId();
		double amount = withdrawTx.getAmount();
		accountsService.withdraw(accountId, amount);
	}

	private void executeDepositForAccount(DepositTransaction depositTx) throws TransactionException {
		String accountId = depositTx.getAccountId();
		double amount = depositTx.getAmount();
		accountsService.deposit(accountId, amount);
	}

}
