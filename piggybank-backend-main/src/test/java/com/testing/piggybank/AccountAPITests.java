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






}


