package com.hs.samples.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.hs.samples.TransactionSampleApplication;
import com.hs.samples.model.Transaction;
import com.hs.samples.model.dto.StatisticDTO;
import com.hs.samples.provider.StatisticDTOProvider;
import com.hs.samples.provider.TransactionProvider;
import com.hs.samples.service.StatisticService;
import com.hs.samples.service.TransactionService;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TransactionSampleApplication.class)
public class TransactionIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private TransactionService transactionService;
	
	@Mock
	private StatisticService statisticService;
	private Transaction transaction;
	
	private TransactionController transactionController;
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	public static final String TRANSACTION =  "{" + "\"amount\":\"632\"," + "\"timestamp\":\"1478192204000\"" + "}"; 

	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		transaction = new TransactionProvider().createTransaction();
		transactionController = new TransactionController(transactionService, statisticService);
	}
	
	@Test
	public void createTransactionGivenTransactionTimestampIsOlderThan60SecondsThenReturnHTTPNoContent() throws Exception {	
		Mockito.when(transactionService.createTransactionRequest(transaction)).thenReturn(transaction);
		
		MvcResult result = mockMvc.perform(post("/api/app/transactionsample/v1/transactions").contentType(contentType).content(TRANSACTION).accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent()).andReturn();
		
	}
	
	@Test
	public void createTransactionGivenTransactionTimestampIsValidThenReturnHTTPCreated() throws Exception {
		long currentTime = System.currentTimeMillis();
		String NEW_TRANSACTION =  "{" + "\"amount\":\"632\"," + "\"timestamp\":\"" + currentTime + "\"" + "}";
		Mockito.when(transactionService.createTransactionRequest(transaction)).thenReturn(transaction);
		
		MvcResult result = mockMvc.perform(post("/api/app/transactionsample/v1/transactions").contentType(contentType).content(NEW_TRANSACTION).accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated()).andReturn();
	}
	
	@Test
	public void getStatistics() throws Exception {
		long currentTime = System.currentTimeMillis();
		
		StatisticDTO statisticDTO = new StatisticDTOProvider().createStatisticDTO();
		Mockito.when(statisticService.getStatistics(currentTime)).thenReturn(statisticDTO);
		MvcResult result = mockMvc.perform(get("/api/app/transactionsample/v1/statistics")).andDo(print()).andExpect(status().isOk()).andReturn();
		
	}

}
