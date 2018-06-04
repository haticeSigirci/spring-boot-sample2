package com.hs.samples;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hs.samples.model.Transaction;
import com.hs.samples.repository.TransactionRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableSwagger2
public class TransactionSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionSampleApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(TransactionRepository tr) {
		return args -> {
			
			Transaction transaction = new Transaction();
			transaction.setAmount(12.5);
			transaction.setTimestamp(System.currentTimeMillis());
			
			tr.save(transaction);
	
		};
	}

}
