package com.hs.samples.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hs.samples.model.Transaction;
import com.hs.samples.provider.TransactionProvider;
import com.hs.samples.repository.TransactionRepository;

public class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	private TransactionService transactionService;
	private Transaction transaction;
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		transactionService = new TransactionService(transactionRepository);
		transaction = new TransactionProvider().createTransaction();
	}
	
	@Test
	public void createTransactionRequestGivenTransactionNotNullWhenCreatingTransactionThenReturnValidTransaction() {
		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
		Transaction result = transactionService.createTransactionRequest(transaction);
		assertEquals(result, transaction);
	}
	
	@Test
	public void getTransactionsIn60SecondsGivenTimestampWhenGettingTransactionListThenReturnTransactionList() {
		long currentTime = System.currentTimeMillis();
		List<Transaction> transactionList = new ArrayList<>();
		Mockito.when(transactionRepository.findAllByTimestampGreaterThanEqual(currentTime)).thenReturn(transactionList);	
		List<Transaction> result = transactionService.getTransactionsIn60Seconds(currentTime);
		assertEquals(result, transactionList);	
	}
	
}
