package com.testing.piggybank;

import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.helper.CurrencyConverterService;
import com.testing.piggybank.model.Account;
import com.testing.piggybank.transaction.CreateTransactionRequest;
import com.testing.piggybank.model.Direction;
import com.testing.piggybank.model.Transaction;
import com.testing.piggybank.transaction.TransactionRepository;
import com.testing.piggybank.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class PiggyBankApplicationTests {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private CurrencyConverterService converterService;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private TransactionService transactionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetTransactions() {
		// Mock data
		long accountId = 1L;
		Integer limit = 5;
		Transaction transaction1 = new Transaction();
		Transaction transaction2 = new Transaction();
		List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

		// Mock behavior
		when(transactionRepository.findAll()).thenReturn(transactions);
		when(accountService.getAccount(accountId)).thenReturn(Optional.of(new Account()));

		// Call the method under test
		List<Transaction> result = transactionService.getTransactions(limit, accountId);

		// Verify the results
		assertEquals(2, result.size());  // Adjust this based on the actual behavior
		// Add more assertions based on your actual implementation
	}

	@Test
	void testCreateTransaction() {
		// Mock data
		CreateTransactionRequest request = new CreateTransactionRequest();
		Transaction transaction = new Transaction();

		// Mock behavior
		when(converterService.toEuro(any(), any())).thenReturn(BigDecimal.TEN);
		when(accountService.getAccount(anyLong())).thenReturn(Optional.of(new Account()));

		// Call the method under test
		transactionService.createTransaction(request);

		// Verify the interactions
		verify(accountService, times(2)).updateBalance(anyLong(), any(), any());
		verify(transactionRepository, times(1)).save(any());
		// Add more verifications based on your actual implementation
	}
}
