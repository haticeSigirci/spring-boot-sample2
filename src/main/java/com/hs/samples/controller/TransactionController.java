package com.hs.samples.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs.samples.exception.TransactionException;
import com.hs.samples.model.Transaction;
import com.hs.samples.model.dto.StatisticDTO;
import com.hs.samples.service.StatisticService;
import com.hs.samples.service.TransactionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/app/transactionsample/v1")
public class TransactionController {

	private final Logger log = LoggerFactory.getLogger(TransactionController.class);
	private static final int BEFORE_60_SECONDS = 60000;

	private final TransactionService transactionService;
	private final StatisticService statisticService;

	public TransactionController(TransactionService transactionService, StatisticService statisticService) {
		this.transactionService = transactionService;
		this.statisticService = statisticService;
	}

	@GetMapping("/statistics")
	@ApiOperation(value = "get statistics information")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Statistics information is successfull"),
			@ApiResponse(code = 204, message = "There is no statistics information") })
	public ResponseEntity<StatisticDTO> getStatistics() {
		log.debug("REST request to get all transaction statistics");
		long currentEpocTimeMillis = System.currentTimeMillis();
		StatisticDTO statistics = this.statisticService.getStatistics(currentEpocTimeMillis-BEFORE_60_SECONDS);
		if(statistics == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(statistics, null, HttpStatus.OK);
	}

	@PostMapping("/transactions")
	@ApiOperation(value = "create transaction")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Transaction Created"),
			@ApiResponse(code = 204, message = "Transaction is older than 60 Seconds") })
	public ResponseEntity<Void> createTransaction(@Valid @RequestBody Transaction transaction) throws TransactionException {
		log.debug("REST request to create Transaction : {}", transaction);
		if ((System.currentTimeMillis() - transaction.getTimestamp()) > 60000) {
			return new ResponseEntity<Void>(null, null, HttpStatus.NO_CONTENT);
		}
		Transaction result = this.transactionService.createTransactionRequest(transaction);
		if (result == null) {
			throw new TransactionException("Transaction could not create");
		}
		return new ResponseEntity<Void>(null, null, HttpStatus.CREATED);
	}

}
