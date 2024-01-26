package com.testing.piggybank;
import com.testing.piggybank.account.AccountRepository;
import com.testing.piggybank.transaction.*;
import com.testing.piggybank.transaction.TransactionRepository;
import com.testing.piggybank.helper.CurrencyConverterService;
import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Currency;
import com.testing.piggybank.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountAPITests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TransactionController transactionController;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void beforeEach(){
        transactionRepository.deleteAll();

    }

    @Test
    void testGetAllTransactions(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", "1");

        ResponseEntity<GetTransactionsResponse> response = restTemplate
                .getForEntity("/api/v1/transactions/1", GetTransactionsResponse.class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful()    );
    }

    @Test
    void createTransaction_withValidTransaction_storesTransactionInDatabase(){
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setCurrency(Currency.EURO);
        request.setReceiverAccountId(1L);
        request.setSenderAccountId(2L);
        request.setDescription("integration test transaction");
        request.setAmount(new BigDecimal(100));

        transactionController.createTransaction(request);

        List<Transaction> result = transactionRepository.findAllByReceiverAccount_id(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void test() {
        List<Account> result = accountRepository.findAll();
        Assertions.assertEquals(4, result.size());
    }

    @Test
    public void TestGetAllTransactions(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", "1");

        ResponseEntity<GetTransactionsResponse> response = restTemplate
                .getForEntity("/api/v1/transactions/1", GetTransactionsResponse.class);

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void test_createTransaction_responseOk(){
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAmount(new BigDecimal(100));
        createTransactionRequest.setReceiverAccountId(2L);
        createTransactionRequest.setSenderAccountId(1L);
        createTransactionRequest.setCurrency(Currency.EURO);
        createTransactionRequest.setDescription("test transactie");

        HttpEntity<CreateTransactionRequest> request = new HttpEntity<>(createTransactionRequest);

        ResponseEntity<HttpStatus> response = restTemplate.postForEntity("/api/v1/transactions", request, HttpStatus.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}


