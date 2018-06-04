package com.hs.samples.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hs.samples.model.Transaction;
import com.hs.samples.model.dto.StatisticDTO;
import com.hs.samples.provider.TransactionProvider;
import com.hs.samples.repository.StatisticRepository;

public class StatisticServiceTest {
	
	@Mock
	private StatisticRepository statisticRepository;
	@Mock
	private TransactionService transactionService;
	
	private StatisticService statisticService;
	private Transaction transaction;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.statisticService = new StatisticService(statisticRepository, transactionService);
		transaction = new TransactionProvider().createTransaction();
	}
	
	@Test
	public void getStatisticsGivenTimeNotNullWhenGettingStatisticDataThenReturnValidStatistics() {
		long currentTime = System.currentTimeMillis();
		List<Transaction> transactionList = new ArrayList<>();
		transaction.setTimestamp(currentTime);
		transactionList.add(transaction);
		Mockito.when(transactionService.getTransactionsIn60Seconds(currentTime)).thenReturn(transactionList);
		StatisticDTO result = this.statisticService.getStatistics(currentTime);
		Mockito.verify(transactionService).getTransactionsIn60Seconds(currentTime);
	}
	
}
