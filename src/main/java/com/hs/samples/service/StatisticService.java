package com.hs.samples.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hs.samples.model.Transaction;
import com.hs.samples.model.dto.StatisticDTO;
import com.hs.samples.repository.StatisticRepository;

@Service
@Transactional
public class StatisticService {
	
	private StatisticRepository statisticRepository;
	private TransactionService transactionService;
	
	public StatisticService(StatisticRepository statisticRepository, TransactionService transactionService) {
		this.statisticRepository = statisticRepository;
		this.transactionService = transactionService;
	}
	
	public StatisticDTO getStatistics(long timeMillis) {
		List<Transaction> transactionList = this.transactionService.getTransactionsIn60Seconds(timeMillis);
		double min = transactionList.stream().min(Comparator.comparing(Transaction::getAmount)).get().getAmount();
		double max = transactionList.stream().max(Comparator.comparing(Transaction::getAmount)).get().getAmount();
		int count = transactionList.size();
		double sum = transactionList.stream().map(Transaction::getAmount).collect(Collectors.summingDouble(i->i));
		double avg = sum / count;
		return new StatisticDTO(sum, avg, max, min, count);
	}

}
