package com.testing.piggybank;
import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.helper.CurrencyConverterService;
import com.testing.piggybank.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import com.testing.piggybank.transaction.CreateTransactionRequest;
import com.testing.piggybank.transaction.TransactionRepository;
import com.testing.piggybank.transaction.TransactionService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.testing.piggybank.model.Transaction;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    public void testGetTransactions(){
        int limit = 5;
        long accountId = 1L;

        List<Transaction> transactions = new ArrayList<>();
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactions(limit, accountId);
        verify(transactionRepository, times(1)).findAll();

        assertEquals(transactions, result);
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

    @Test
    public void testSortDescByDateTime() {
        Transaction t1 = new Transaction();
        t1.setDateTime(Instant.parse("2024-01-23T12:30:00Z"));

        Transaction t2 = new Transaction();
        t2.setDateTime(Instant.parse("2024-01-23T13:45:00Z"));

        int result = TransactionService.sortDescByDateTime(t1, t2);

        assertEquals(1, result);
    }

    @Test
    public void testFilterAndLimitTransactions() {
        long accountId = 1L;
        Integer limit = 1;

        Account senderAccount = new Account();
        senderAccount.setId(accountId);
        senderAccount.setName("Tijntje");
        senderAccount.setBalance(BigDecimal.valueOf(75));

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setName("Melvin");
        receiverAccount.setBalance(BigDecimal.valueOf(50));

        List<Transaction> mockedTransactions = new ArrayList<>();

        Transaction tijnsEersteTransactie = new Transaction();
        tijnsEersteTransactie.setSenderAccount(senderAccount);
        tijnsEersteTransactie.setReceiverAccount(receiverAccount);
        tijnsEersteTransactie.setAmount(BigDecimal.TEN);
        tijnsEersteTransactie.setCurrency(Currency.EURO);
        tijnsEersteTransactie.setDescription("eerste voorbeeldtransactie");
        tijnsEersteTransactie.setDateTime(Instant.now().plusMillis(1));
        tijnsEersteTransactie.setStatus(Status.SUCCESS);
        mockedTransactions.add(tijnsEersteTransactie);

        Transaction tijnsTweedeTransactie = new Transaction();
        tijnsTweedeTransactie.setSenderAccount(senderAccount);
        tijnsTweedeTransactie.setReceiverAccount(receiverAccount);
        tijnsTweedeTransactie.setAmount(BigDecimal.TEN);
        tijnsTweedeTransactie.setCurrency(Currency.EURO);
        tijnsTweedeTransactie.setDescription("tweede voorbeeldtransactie");
        tijnsTweedeTransactie.setDateTime(Instant.now());
        tijnsTweedeTransactie.setStatus(Status.SUCCESS);
        mockedTransactions.add(tijnsTweedeTransactie);

        Transaction tijnsDerdeTransactie = new Transaction();
        tijnsDerdeTransactie.setSenderAccount(senderAccount);
        tijnsDerdeTransactie.setReceiverAccount(receiverAccount);
        tijnsDerdeTransactie.setAmount(BigDecimal.TEN);
        tijnsDerdeTransactie.setCurrency(Currency.EURO);
        tijnsDerdeTransactie.setDescription("derde voorbeeldtransactie");
        tijnsDerdeTransactie.setDateTime(Instant.now());
        tijnsDerdeTransactie.setStatus(Status.SUCCESS);
        mockedTransactions.add(tijnsDerdeTransactie);

        List<Transaction> result = transactionService.filterAndLimitTransactions(mockedTransactions, accountId, limit);

        assertEquals(1, result.size());
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











