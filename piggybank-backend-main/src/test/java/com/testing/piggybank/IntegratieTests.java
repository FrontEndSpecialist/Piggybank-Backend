package com.testing.piggybank;
import com.testing.piggybank.account.AccountRepository;
import com.testing.piggybank.model.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void test() {
        List<Account> result = accountRepository.findAll();

        Assertions.assertEquals(4, result.size());
    }

}