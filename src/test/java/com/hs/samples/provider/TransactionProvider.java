package com.hs.samples.provider;

import com.hs.samples.model.Transaction;

public class TransactionProvider {
	
	private Transaction transaction;
	
	public Transaction createTransaction() {
		transaction = new Transaction();
		transaction.setAmount(12.5);
		transaction.setTimestamp(1478192204000l);
		return transaction;
	}

}
