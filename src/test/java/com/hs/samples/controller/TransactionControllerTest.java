package com.hs.samples.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hs.samples.exception.TransactionException;
import com.hs.samples.model.Transaction;
import com.hs.samples.model.dto.StatisticDTO;
import com.hs.samples.provider.StatisticDTOProvider;
import com.hs.samples.provider.TransactionProvider;
import com.hs.samples.service.StatisticService;
import com.hs.samples.service.TransactionService;

public class TransactionControllerTest {

	private static final int BEFORE_60_SECONDS = 60000;

	@Mock
	private TransactionService transactionService;
	@Mock
	private StatisticService statisticService;

	private TransactionController transactionController;
	private Transaction transaction;
	private StatisticDTO statisticsDto;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		transactionController = new TransactionController(transactionService, statisticService);
		transaction = new TransactionProvider().createTransaction();
		statisticsDto = new StatisticDTOProvider().createStatisticDTO();
	}
	
	@Test
	public void craeteTransactionGivenTransactionIsOlderThan60SecondsWhenCreatingTransactionThenReturnHTTP204() throws TransactionException {
		Mockito.when(transactionService.createTransactionRequest(transaction)).thenReturn(transaction);
		ResponseEntity<Void> result = transactionController.createTransaction(transaction);
		assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);
	}
	
	@Test
	public void createTransactionGivenTransactionIsLessThan60SecondsWhenCreatingTransactionThenReturnHTTP201() throws TransactionException {
		transaction.setTimestamp(System.currentTimeMillis());
		Mockito.when(transactionService.createTransactionRequest(transaction)).thenReturn(transaction);
		ResponseEntity<Void> result = transactionController.createTransaction(transaction);
		assertEquals(result.getStatusCode(), HttpStatus.CREATED);
		Mockito.verify(transactionService).createTransactionRequest(transaction);
	}
	
	@Test
	public void getStatisticsWhenGettingStatisticDataAndNoTransactionThenReturnHTTPNoContent() {
		long currentTime = System.currentTimeMillis();
		Mockito.when(statisticService.getStatistics(currentTime)).thenReturn(statisticsDto);
		ResponseEntity<StatisticDTO> result = transactionController.getStatistics();
		assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);
	}
	
	@Test
	public void getStatisticsWhenGettingStatisticDataThenReturnValidStatisticDTO() throws TransactionException {
		long currentTime = System.currentTimeMillis();
		//transaction.setTimestamp(currentTime);
		Mockito.when(statisticService.getStatistics(currentTime-BEFORE_60_SECONDS)).thenReturn(statisticsDto);
		ResponseEntity<StatisticDTO> result = transactionController.getStatistics();
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}
	
}
