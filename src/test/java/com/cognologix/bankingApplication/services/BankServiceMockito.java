package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.*;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import com.cognologix.bankingApplication.services.implementation.BankOperationServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BankOperationsServiceTest.class})
public class BankServiceMockito {

    @Mock
    BankAccountRepository bankAccountRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    BankOperationServiceImplementation bankOperationServiceImplementation;

    AccountDto accountDto = new AccountDto(1,
            "Savings", 1000.0, 1);

    Customer customer = new Customer(1, "Onkar", LocalDate.of(1998, 11, 11),
            "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");

    Account account = new Account(accountDto.getAccountID(), "Activate", accountDto.getAccountType(),
            1000L, accountDto.getBalance(), customer);

    Account accountForReceiveMoney = new Account(2, "Activate", "Current",
            1001L, 10000.00, customer);

    List<Account> accounts = new ArrayList<>();
    BankTransaction transaction = new BankTransaction();


    @Test
    public void testCreateAccount() {

        CreateAccountResponseDto createAccountResponseDto=new CreateAccountResponseDto();
        createAccountResponseDto.setCustomerName(account.getCustomer().getCustomerName());
        createAccountResponseDto.setAccountNumber(account.getAccountNumber());
        createAccountResponseDto.setAccountType(account.getAccountType());
        createAccountResponseDto.setStatus(account.getStatus());
        createAccountResponseDto.setBalance(account.getBalance());

        when(customerRepository.findByCustomerIdEquals(accountDto.getCustomerId())).thenReturn(customer);
        when(bankAccountRepository.findAll()).thenReturn(accounts);
        when(bankAccountRepository.save(account)).thenReturn(account);

        CreatedAccountResponse expected = new CreatedAccountResponse(true,
                "Account created successfully...", createAccountResponseDto);

        CreatedAccountResponse actual = bankOperationServiceImplementation.createAccount(accountDto);

        assertEquals(actual, expected);
    }

    @Test
    public void testGetAccountByAccountNumber() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        Account actual = bankOperationServiceImplementation.getAccountByAccountNumber(account.getAccountNumber());
        Account expected = account;
        assertEquals(expected, actual);

    }

    @Test
    public void testDepositAmount() {
        Double amountToDeposit = 500.00;
        Double updatedBalance = account.getBalance() + amountToDeposit;

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        DepositAmountResponse actual = bankOperationServiceImplementation.deposit(account.getAccountNumber(), amountToDeposit);

        DepositAmountResponse expected = new DepositAmountResponse(true,
                amountToDeposit + " deposited successfully... \nAvailable balance is : " + updatedBalance);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithdrawAmount() {
        Double amountToWithdraw = 500.00;
        Double updatedBalance = account.getBalance() - amountToWithdraw;

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        WithdrawAmountResponse actual = bankOperationServiceImplementation.withdraw(account.getAccountNumber(), amountToWithdraw);

        WithdrawAmountResponse expected = new WithdrawAmountResponse(true,
                amountToWithdraw + " withdraw successfully... \nAvailable balance is : " + updatedBalance);
        assertEquals(expected, actual);
    }

    @Test
    public void testMoneyTransfer() {
        Long fromAccount = account.getAccountNumber();
        Long toAccount = accountForReceiveMoney.getAccountNumber();
        Double amountForTransfer = 300.00;
        Double updatedBalance = account.getBalance() - amountForTransfer;

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(accountForReceiveMoney.getAccountNumber())).thenReturn(accountForReceiveMoney);
        when(bankAccountRepository.save(accountForReceiveMoney)).thenReturn(accountForReceiveMoney);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        TransferAmountResponse expected = bankOperationServiceImplementation.moneyTransfer(fromAccount, toAccount, amountForTransfer);
        TransferAmountResponse actual = new TransferAmountResponse(true,
                "Amount " + amountForTransfer + " transferred successfully... \nRemaining balance is " + updatedBalance);

        assertEquals(expected, actual);
    }

    @Test
    public void testActivateAccountByAccountNumber() {

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        Account activatedAccount = account;
        activatedAccount.setAccountType("Active");
        when(bankAccountRepository.save(activatedAccount)).thenReturn(activatedAccount);
        ActivateAccountResponse actual = bankOperationServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber());
        ActivateAccountResponse expected = new ActivateAccountResponse(true, "Successfully activated " + activatedAccount.getAccountNumber() + " number account");

        assertEquals(expected, actual);
    }

    Account deactivatedAccount;
    List<Account> deactivatedAccounts = new ArrayList<>();

    @Test
    public void testDeactivateAccountByAccountNumber() {

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        deactivatedAccount = account;
        deactivatedAccount.setAccountType("deactivated");
        when(bankAccountRepository.save(deactivatedAccount)).thenReturn(deactivatedAccount);

        DeactivateAccountResponse actual = bankOperationServiceImplementation.deactivateAccountByAccountNumber(account.getAccountNumber());
        DeactivateAccountResponse expected = new DeactivateAccountResponse(true, "Successfully deactivated " + deactivatedAccount.getAccountNumber() + " number account");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllDeactivatedAccounts() {
        deactivatedAccount = account;
        deactivatedAccount.setAccountType("deactivated");

        //for searching deactivated accounts list
        deactivatedAccounts.add(deactivatedAccount);

        when(bankAccountRepository.findDeactivatedAccounts()).thenReturn(deactivatedAccounts);

        DeactivatedAccountsResponse actual = bankOperationServiceImplementation.getAllDeactivatedAccounts();
        DeactivatedAccountsResponse expected = new DeactivatedAccountsResponse(true, "Deactivated accounts are...", deactivatedAccounts);

        assertEquals(expected, actual);
    }
}
