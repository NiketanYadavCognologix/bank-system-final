package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.entities.Account;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {AccountDaoTest.class})
public class AccountDaoTest {
    @Mock
    BankAccountRepository bankAccountRepository;

    @Test
    public void testFindByAccountNumberEquals(){
       Account account = bankAccountRepository.findByAccountNumberEquals(1000L);
        System.out.println(account);
    }
}
