package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.*;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import com.cognologix.bankingApplication.enums.AccountStatus;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.enums.responseMessages.ForAccount;
import com.cognologix.bankingApplication.exceptions.*;
import com.cognologix.bankingApplication.services.implementation.BankOperationServiceImplementation;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BankServiceMockito.class})
@RunWith(SpringRunner.class)
public class BankServiceMockito {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BankOperationServiceImplementation bankOperationServiceImplementation;

    AccountDto accountDto = new AccountDto(1L, "Savings", 1000.0, 1);

    Customer customer = new Customer(1, "Onkar", LocalDate.of(1998, 11, 11), "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");

    Account account = new Account(accountDto.getAccountNumber(), "Activate", accountDto.getAccountType(), accountDto.getBalance(), customer);

    Account accountForReceiveMoney = new Account(2L, "Activate", "Cuarrent", 10000.00, customer);

    List<Account> accounts = new ArrayList<>();
    BankTransaction transaction = new BankTransaction();
    Account deactivatedAccount;
    List<Account> deactivatedAccounts = new ArrayList<>();

    @BeforeEach
    @Test
    public void testCreateAccount() {
        when(customerRepository.findByCustomerIdEquals(accountDto.getCustomerId())).thenReturn(customer);
        when(bankAccountRepository.save(account)).thenReturn(account);
        CreatedAccountResponse expected = new CreatedAccountResponse(true, ForAccount.CreateAccount.name(), account);

        CreatedAccountResponse actual = bankOperationServiceImplementation.createAccount(accountDto);
        assertEquals(expected, actual);

    }

    @Test
    public void test_createAccount_IllegalTypeOfAccountException() {
        accountDto = new AccountDto(null, "Svings", 1000.0, 1);

        when(bankAccountRepository.save(account)).thenThrow(new IllegalTypeOfAccountException(ErrorsForAccount.illegalTypeOFAccount.getMessage()));
        IllegalTypeOfAccountException exception = assertThrows(IllegalTypeOfAccountException.class, () -> bankOperationServiceImplementation.createAccount(accountDto));
        assertEquals(ErrorsForAccount.illegalTypeOFAccount.getMessage(), exception.getMessage());
    }

    @Test
    public void test_createAccount_CustomerNotFoundException() {
        accountDto = new AccountDto(null, "Savings", 1000.0, 33);
        when(customerRepository.findByCustomerIdEquals(accountDto.getCustomerId())).thenThrow(new CustomerNotFoundException(ErrorsForCustomer.customerNotFound.getMessage()));
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> bankOperationServiceImplementation.createAccount(accountDto));
        assertEquals(ErrorsForCustomer.customerNotFound.getMessage(), exception.getMessage());

    }

    @Test
    public void testGetAccountByAccountNumber() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        Account actual = bankOperationServiceImplementation.getAccountByAccountNumber(account.getAccountNumber());
        Account expected = account;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAccountByAccountNumber_AccountNotAvailableException() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenThrow(new AccountNotAvailableException("Account not available"));
        AccountNotAvailableException exception = assertThrows(AccountNotAvailableException.class, () -> bankOperationServiceImplementation.getAccountByAccountNumber(account.getAccountNumber()));
        assertEquals(ErrorsForAccount.accountNotAvailable.getMessage(), exception.getMessage());
    }


    @Test
    public void testDepositAmount() {
        Double amountToDeposit = 500.00;
        Double updatedBalance = account.getBalance() + amountToDeposit;

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        when(transactionRepository.save(null)).thenReturn(null);
        DepositAmountResponse actual = bankOperationServiceImplementation.depositAmount(account.getAccountNumber(), amountToDeposit);

        DepositAmountResponse expected = new DepositAmountResponse(true, amountToDeposit + ForAccount.depositAmount.name());
        assertEquals(expected, actual);
    }

    @Test
    public void testDepositAmount_DeactivateAccountException() {
        Account account = new Account(accountDto.getAccountNumber(), AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())).thenThrow(new DeactivateAccountException(ErrorsForAccount.deactivateAccount.getMessage()));
        DeactivateAccountException exception = assertThrows(DeactivateAccountException.class, () -> bankOperationServiceImplementation.depositAmount(account.getAccountNumber(), 500.00));
        assertEquals(ErrorsForAccount.deactivateAccount.getMessage(), exception.getMessage());

    }

    @Test
    public void testWithdrawAmount() {
        Double amountToWithdraw = 500.00;
        Double updatedBalance = account.getBalance() - amountToWithdraw;

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        when(transactionRepository.save(null)).thenReturn(null);

        WithdrawAmountResponse actual = bankOperationServiceImplementation.withdrawAmount(account.getAccountNumber(), amountToWithdraw);

        WithdrawAmountResponse expected = new WithdrawAmountResponse(true, amountToWithdraw + "withdrawAmount");
        assertEquals(expected, actual);
    }

    @Test
    public void testWithdrawAmount_DeactivateAccountException() {
        Account account = new Account(accountDto.getAccountNumber(), AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())).thenThrow(new DeactivateAccountException(ErrorsForAccount.deactivateAccount.getMessage()));
        DeactivateAccountException exception = assertThrows(DeactivateAccountException.class, () -> bankOperationServiceImplementation.withdrawAmount(account.getAccountNumber(), 500.00));
        assertEquals(ErrorsForAccount.deactivateAccount.getMessage(), exception.getMessage());

    }

    @Test
    public void testWithdrawAmount_InsufficientBalanceException() {

        Double amountToWithdraw = 500000.00;
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getBalance() < amountToWithdraw).thenThrow(new InsufficientBalanceException(ErrorsForAccount.insufficientBalance.getMessage()));
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> bankOperationServiceImplementation.withdrawAmount(account.getAccountNumber(), amountToWithdraw));
        assertEquals(ErrorsForAccount.insufficientBalance.getMessage(), exception.getMessage());


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
        when(transactionRepository.save(null)).thenReturn(null);

        TransferAmountResponse expected = bankOperationServiceImplementation.moneyTransfer(fromAccount, toAccount, amountForTransfer);
        TransferAmountResponse actual = new TransferAmountResponse(true, amountForTransfer + ForAccount.transferAmount.name() + updatedBalance);

        assertEquals(expected, actual);
    }

    @Test
    public void testMoneyTransfer_DeactivateAccountException() {

        Account account = new Account(accountDto.getAccountNumber(), AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())).thenThrow(new DeactivateAccountException(ErrorsForAccount.deactivateAccount.getMessage()));
        DeactivateAccountException exception = assertThrows(DeactivateAccountException.class, () -> bankOperationServiceImplementation.moneyTransfer(account.getAccountNumber(), accountForReceiveMoney.getAccountNumber(), 500.20));
        assertEquals(ErrorsForAccount.deactivateAccount.getMessage(), exception.getMessage());
    }

    @Test
    public void testMoneyTransfer_InsufficientBalanceException() {

        Double amountToWithdraw = 500000.00;
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getBalance() < amountToWithdraw).thenThrow(new InsufficientBalanceException(ErrorsForAccount.insufficientBalance.getMessage()));
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> bankOperationServiceImplementation.moneyTransfer(account.getAccountNumber(), accountForReceiveMoney.getAccountNumber(), 500.20));
        assertEquals(ErrorsForAccount.insufficientBalance.getMessage(), exception.getMessage());

    }

    @Test
    public void testActivateAccountByAccountNumber() {
        Account account = new Account(accountDto.getAccountNumber(), AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        Account activatedAccount = account;
        when(bankAccountRepository.save(activatedAccount)).thenReturn(activatedAccount);
        ActivateAccountResponse actual = bankOperationServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber());
        ActivateAccountResponse expected = new ActivateAccountResponse(true, ForAccount.activatedAccount.name());
        assertEquals(expected, actual);
    }

    @Test
    public void testActivateAccountByAccountNumber_AccountNotAvailableException() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenThrow(new AccountNotAvailableException(ErrorsForAccount.accountNotAvailable.getMessage()));
        AccountNotAvailableException exception = assertThrows(AccountNotAvailableException.class, () -> bankOperationServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber()));

        assertEquals(ErrorsForAccount.accountNotAvailable.getMessage(), exception.getMessage());
    }

    @Test
    public void testActivateAccountByAccountNumber_AccountAlreadyActivatedException() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).equals(AccountStatus.ACTIVATED)).thenThrow(new AccountAlreadyActivatedException(ErrorsForAccount.accountAlreadyActivate.getMessage()));
        AccountAlreadyActivatedException exception = assertThrows(AccountAlreadyActivatedException.class, () -> bankOperationServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber()));

        assertEquals(ErrorsForAccount.accountAlreadyActivate.getMessage(), exception.getMessage());
    }

    @Test
    public void testDeactivateAccountByAccountNumber() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        deactivatedAccount = account;
        deactivatedAccount.setAccountType(AccountStatus.DEACTIVATED.name());
        when(bankAccountRepository.save(deactivatedAccount)).thenReturn(deactivatedAccount);

        DeactivateAccountResponse actual = bankOperationServiceImplementation.deactivateAccountByAccountNumber(account.getAccountNumber());
        DeactivateAccountResponse expected = new DeactivateAccountResponse(true, ForAccount.deactivateAccount.name());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeactivateAccountByAccountNumber_AccountNotAvailableException() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenThrow(new AccountNotAvailableException(ErrorsForAccount.accountNotAvailable.getMessage()));
        AccountNotAvailableException exception = assertThrows(AccountNotAvailableException.class, () -> bankOperationServiceImplementation.deactivateAccountByAccountNumber(account.getAccountNumber()));
        assertEquals(ErrorsForAccount.accountNotAvailable.getMessage(), exception.getMessage());
    }

    @Test
    public void testGetAllDeactivatedAccounts() {

        deactivatedAccount = account;
        deactivatedAccount.setAccountType(AccountStatus.DEACTIVATED.name());

        //for searching deactivated accounts list
        deactivatedAccounts.add(deactivatedAccount);

        when(bankAccountRepository.findDeactivatedAccounts()).thenReturn(deactivatedAccounts);
        DeactivatedAccountsResponse actual = bankOperationServiceImplementation.getAllDeactivatedAccounts();
        DeactivatedAccountsResponse expected = new DeactivatedAccountsResponse(true, ForAccount.listOfDeactivatedAccounts.name(), deactivatedAccounts);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllDeactivatedAccounts_AccountNotAvailableException() throws AccountNotAvailableException {
        when(bankAccountRepository.findDeactivatedAccounts()).thenReturn(deactivatedAccounts);
        when(bankAccountRepository.findDeactivatedAccounts()).thenThrow(new AccountNotAvailableException(ErrorsForAccount.accountNotAvailable.getMessage()));

        Exception exception = assertThrows(AccountNotAvailableException.class, () -> bankOperationServiceImplementation.getAllDeactivatedAccounts());
        assertEquals(ErrorsForAccount.accountNotAvailable.getMessage(), exception.getMessage());
    }

    @Test
    public void testGetAccountBalance() {

        Long accountNumber = account.getAccountNumber();
        when(bankAccountRepository.findByAccountNumberEquals(accountNumber)).thenReturn(account);

        BalanceInquiryResponse actual = bankOperationServiceImplementation.getAccountBalance(accountNumber);
        BalanceInquiryResponse expected = new BalanceInquiryResponse(true, ForAccount.availableBalance.name() + account.getBalance());

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAccountBalance_AccountNotAvailableException() {
        Long accountNumber = account.getAccountNumber();
        when(bankAccountRepository.findByAccountNumberEquals(accountNumber)).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(accountNumber)).thenThrow(new AccountNotAvailableException(ErrorsForAccount.accountNotAvailable.getMessage()));

        Exception exception = assertThrows(AccountNotAvailableException.class, () -> bankOperationServiceImplementation.getAccountBalance(accountNumber));

        String expected = "Account not available";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

    }


}
