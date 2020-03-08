package com.scratch.transactionprocessor.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scratch.transactionprocessor.errors.AccountNotExistsException;
import com.scratch.transactionprocessor.service.AccountsService;

@RestController
public class AccountsController {
	
	@Autowired
	private AccountsService accountsService;
	
	private final Logger logger = LoggerFactory.getLogger(AccountsController.class);
	
	@GetMapping(value="/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> getAccountBalances(@RequestParam MultiValueMap<String, String> accountMap) {
		
		List<String> accountIds = accountMap.get("accountId");
		List<Map<String, Object>> accounts = new ArrayList<Map<String,Object>>();
		if (accountIds != null && accountIds.size() > 0) {
	 		for (String accountId: accountIds) {
				double balance = 0;
				boolean frozen = false;
				try {
					balance = accountsService.getAccountBalance(accountId);
					frozen = !accountsService.isActive(accountId);
				} catch (AccountNotExistsException e) {
					logger.error("Account not exists {}", accountId);
				}
				
				Map<String, Object> account = constructAccountObj(accountId, balance, frozen);
				accounts.add(account);
			}			
		}
		return accounts;
	}

	private Map<String, Object> constructAccountObj(String accountId, double balance, boolean frozen) {
		Map<String, Object> account = new HashMap<String, Object>();
		account.put("accountId", accountId);
		account.put("balance", balance);
		account.put("frozen", frozen);
		return account;
	}
}
