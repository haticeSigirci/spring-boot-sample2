package com.hs.samples.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hs.samples.model.Transaction;
import com.hs.samples.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService {

	private TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public Transaction createTransactionRequest(Transaction transaction) {		
		return (Transaction) this.transactionRepository.save(transaction);
	}
	
	public List<Transaction> getTransactionsIn60Seconds(long timeMillis) {
		List<Transaction> transactionList = this.transactionRepository.findAllByTimestampGreaterThanEqual(timeMillis);
		return transactionList;
	}

}
