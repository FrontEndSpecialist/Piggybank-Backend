package com.testing.piggybank;
import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.helper.CurrencyConverterService;
import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Currency;
import com.testing.piggybank.model.Direction;
import java.time.LocalDateTime;
import java.util.Collections;



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.testing.piggybank.model.Transaction;
import com.testing.piggybank.transaction.CreateTransactionRequest;
import com.testing.piggybank.transaction.TransactionRepository;
import com.testing.piggybank.transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyConverterService converterService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void test(){
    assertEquals("Dit is hetzelfde", "Dit is hetzelfde");
    var object1 = new Object();
    var object2 = new Object();
    assertNotEquals(object1, object2);

    String string1="testing";
    assertNotNull(string1);
    assertSame(string1, string1);

    String string2 = null;
    assertNull(string2);
    int [] testArray1 = new int [] {1, 2, 3};
    int [] testArray2 = new int [] {1, 2, 3};
    assertArrayEquals(testArray1, testArray2);
    assertTrue(1 < 2);

}

    @Test
    public void testMapRequestToTransaction() {
        // Mock the behavior of accountService
        Account senderAccount = new Account();
        senderAccount.setId(1L);
        Account receiverAccount = new Account();
        receiverAccount.setId(2L);

        when(accountService.getAccount(1L)).thenReturn(Optional.of(senderAccount));
        when(accountService.getAccount(2L)).thenReturn(Optional.of(receiverAccount));

        // Create a test CreateTransactionRequest
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setSenderAccountId(1L);
        createTransactionRequest.setReceiverAccountId(2L);
        createTransactionRequest.setAmount(BigDecimal.valueOf(100));
        createTransactionRequest.setCurrency(Currency.EURO);
        createTransactionRequest.setDescription("Test Transaction");

        // Invoke the method to be tested
        Transaction resultTransaction = transactionService.mapRequestToTransaction(createTransactionRequest);

        // Verify the result
        assertNotNull(resultTransaction);
        assertEquals(createTransactionRequest.getAmount(), resultTransaction.getAmount());
        assertEquals(createTransactionRequest.getCurrency(), resultTransaction.getCurrency());
        assertEquals(createTransactionRequest.getDescription(), resultTransaction.getDescription());
        assertSame(senderAccount, resultTransaction.getSenderAccount());
        assertSame(receiverAccount, resultTransaction.getReceiverAccount());
        assertNotNull(resultTransaction.getDateTime());
    }

    @Test
    public void testCreateTransactionSuccess() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        Transaction transaction = new Transaction();

        when(converterService.toEuro(any(), any())).thenReturn(BigDecimal.TEN);
        when(accountService.getAccount(anyLong())).thenReturn(Optional.of(new Account()));

        transactionService.createTransaction(request);

        verify(accountService, times(2)).updateBalance(anyLong(), any(), any());
        verify(transactionRepository, times(1)).save(any());
    }







}


//    @Test
//    public void testCreateTransaction() {
//        CreateTransactionRequest request = new CreateTransactionRequest();
//        Transaction transaction = new Transaction();
//
//        when(converterService.toEuro(any(), any())).thenReturn(BigDecimal.TEN);
//        when(accountService.getAccount(anyLong())).thenReturn(Optional.of(new Account()));
//
//        transactionService.createTransaction(request);
//
//        verify(accountService, times(2)).updateBalance(anyLong(), any(), any());
//        verify(transactionRepository, times(1)).save(any());
//    }











