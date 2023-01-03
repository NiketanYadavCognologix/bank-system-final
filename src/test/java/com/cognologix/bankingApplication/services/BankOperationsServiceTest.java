package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.BankBranchRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.banks.branch.Branch;
import com.cognologix.bankingApplication.enums.AccountStatus;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.enums.responseMessages.ForAccount;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyActivatedException;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.exceptions.DeactivateAccountException;
import com.cognologix.bankingApplication.exceptions.IllegalTypeOfAccountException;
import com.cognologix.bankingApplication.exceptions.InsufficientBalanceException;
import com.cognologix.bankingApplication.services.implementation.AccountServiceImplementation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BankOperationsServiceTest {

    private static final Logger LOGGER = LogManager.getLogger(BankOperationsServiceTest.class);
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankBranchRepository bankBranchRepository;

    @InjectMocks
    private AccountServiceImplementation accountServiceImplementation;

    AccountDto accountDto = new AccountDto(1L,"SBI","KATRAJ", "Savings", 1000.0, 1);

    Customer customer = new Customer(1, "Onkar", "10-10-1998", "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");

    Account account = new Account(accountDto.getAccountNumber(),"SBI","KATRAJ","SBI001122", "Activate", accountDto.getAccountType(), accountDto.getBalance(), customer);

    Account accountForReceiveMoney = new Account(2L,"SBI","KATRAJ","SBI001122", "Activate", "Cuarrent", 10000.00, customer);

    Branch branch = new Branch(1,"KATRAJ","Katraj, Pune, Maharashtra","SBI001122");

    Account deactivatedAccount;
    List<Account> deactivatedAccounts = new ArrayList<>();

    @BeforeEach
    @Test
    public void testCreateAccount() {
        when(customerRepository.findByCustomerIdEquals(accountDto.getCustomerId())).thenReturn(customer);
        when(bankBranchRepository.findByBranchEquals(accountDto.getBranch())).thenReturn(branch);
        when(bankAccountRepository.save(account)).thenReturn(account);
        CreatedAccountResponse expected = new CreatedAccountResponse(true,
                ForAccount.CREATE_ACCOUNT.getMessage(), account);

        CreatedAccountResponse actual = accountServiceImplementation.createAccount(accountDto);
        assertEquals(expected, actual);
        LOGGER.debug(actual);
    }

    @Test
    public void test_createAccount_IllegalTypeOfAccountException() {
        accountDto = new AccountDto(null,"SBI","KATRAJ", "Svings", 1000.0, 1);

        when(bankAccountRepository.save(account)).thenThrow(new IllegalTypeOfAccountException(ErrorsForAccount.ILLEGAL_TYPE_OF_ACCOUNT.getMessage()));
        IllegalTypeOfAccountException exception = assertThrows(IllegalTypeOfAccountException.class, () -> accountServiceImplementation.createAccount(accountDto));
        assertEquals(ErrorsForAccount.ILLEGAL_TYPE_OF_ACCOUNT.toString(), exception.getMessage());
    }

    @Test
    public void test_createAccount_CustomerNotFoundException() {
        accountDto = new AccountDto(null,"SBI","KATRAJ", "Savings", 1000.0, 33);
        when(customerRepository.findByCustomerIdEquals(accountDto.getCustomerId())).thenThrow(new CustomerNotFoundException(ErrorsForCustomer.CUSTOMER_NOT_FOUND));
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> accountServiceImplementation.createAccount(accountDto));
        assertEquals(ErrorsForCustomer.CUSTOMER_NOT_FOUND.toString(), exception.getMessage());

    }

    @Test
    public void testGetAccountByAccountNumber() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        Account actual = accountServiceImplementation.getAccountByAccountNumber(account.getAccountNumber());
        Account expected = account;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAccountByAccountNumber_AccountNotAvailableException() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenThrow(new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE));
        AccountNotAvailableException exception = assertThrows(AccountNotAvailableException.class, () -> accountServiceImplementation.getAccountByAccountNumber(account.getAccountNumber()));
        assertEquals(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.toString(), exception.getMessage());
    }


    @Test
    public void testDepositAmount() {
        Double amountToDeposit = 500.00;

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        when(transactionRepository.save(null)).thenReturn(null);
        DepositAmountResponse actual = accountServiceImplementation.depositAmount(account.getAccountNumber(), amountToDeposit);

        DepositAmountResponse expected = new DepositAmountResponse(true, amountToDeposit + ForAccount.DEPOSIT_AMOUNT.getMessage());
        assertEquals(expected, actual);
    }

    @Test
    public void testDepositAmount_DeactivateAccountException() {
        Account account = new Account(accountDto.getAccountNumber(), accountDto.getBankName(), accountDto.getBranch(), "SBI001122", AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())).thenThrow(new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT));
        DeactivateAccountException exception = assertThrows(DeactivateAccountException.class, () -> accountServiceImplementation.depositAmount(account.getAccountNumber(), 500.00));
        assertEquals(ErrorsForAccount.DEACTIVATE_ACCOUNT.toString(), exception.getMessage());

    }

    @Test
    public void testWithdrawAmount() {
        Double amountToWithdraw = 500.00;
        Double updatedBalance = account.getBalance() - amountToWithdraw;

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        when(transactionRepository.save(null)).thenReturn(null);

        WithdrawAmountResponse actual = accountServiceImplementation.withdrawAmount(account.getAccountNumber(), amountToWithdraw);

        WithdrawAmountResponse expected = new WithdrawAmountResponse(true, amountToWithdraw + ForAccount.WITHDRAW_AMOUNT.getMessage());
        assertEquals(expected, actual);
    }

    @Test
    public void testWithdrawAmount_DeactivateAccountException() {
        Account account = new Account(accountDto.getAccountNumber(), accountDto.getBankName(), accountDto.getBranch(), "SBI001122", AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())).thenThrow(new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT));
        DeactivateAccountException exception = assertThrows(DeactivateAccountException.class, () -> accountServiceImplementation.withdrawAmount(account.getAccountNumber(), 500.00));
        assertEquals(ErrorsForAccount.DEACTIVATE_ACCOUNT.toString(), exception.getMessage());

    }

    @Test
    public void testWithdrawAmount_InsufficientBalanceException() {

        Double amountToWithdraw = 500000.00;
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getBalance() < amountToWithdraw).thenThrow(new InsufficientBalanceException(ErrorsForAccount.INSUFFICIENT_BALANCE));
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> accountServiceImplementation.withdrawAmount(account.getAccountNumber(), amountToWithdraw));
        assertEquals(ErrorsForAccount.INSUFFICIENT_BALANCE.toString(), exception.getMessage());


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

        TransferAmountResponse expected = accountServiceImplementation.moneyTransfer(fromAccount, toAccount, amountForTransfer);
        TransferAmountResponse actual = new TransferAmountResponse(true, amountForTransfer + ForAccount.TRANSFER_AMOUNT.getMessage() + updatedBalance);

        assertEquals(expected, actual);
    }

    @Test
    public void testMoneyTransfer_DeactivateAccountException() {

        Account account = new Account(accountDto.getAccountNumber(), accountDto.getBankName(), accountDto.getBranch(),"SBI001122", AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())).thenThrow(new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT));
        DeactivateAccountException exception = assertThrows(DeactivateAccountException.class, () -> accountServiceImplementation.moneyTransfer(account.getAccountNumber(), accountForReceiveMoney.getAccountNumber(), 500.20));
        assertEquals(ErrorsForAccount.DEACTIVATE_ACCOUNT.toString(), exception.getMessage());
    }

    @Test
    public void testMoneyTransfer_InsufficientBalanceException() {

        Double amountToWithdraw = 500000.00;
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).getBalance() < amountToWithdraw).thenThrow(new InsufficientBalanceException(ErrorsForAccount.INSUFFICIENT_BALANCE));
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> accountServiceImplementation.moneyTransfer(account.getAccountNumber(), accountForReceiveMoney.getAccountNumber(), 50000.20));
        assertEquals(ErrorsForAccount.INSUFFICIENT_BALANCE.toString(), exception.getMessage());

    }

    @Test
    public void testActivateAccountByAccountNumber() {
        Account account = new Account(accountDto.getAccountNumber(), accountDto.getBankName(), accountDto.getBranch(),"SBI001122", AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);

        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        Account activatedAccount = account;
        when(bankAccountRepository.save(activatedAccount)).thenReturn(activatedAccount);
        ActivateAccountResponse actual = accountServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber());
        ActivateAccountResponse expected = new ActivateAccountResponse(true, ForAccount.ACTIVATED_ACCOUNT.getMessage());
        assertEquals(expected, actual);
    }

    @Test
    public void testActivateAccountByAccountNumber_AccountNotAvailableException() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenThrow(new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE));
        AccountNotAvailableException exception = assertThrows(AccountNotAvailableException.class, () -> accountServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber()));

        assertEquals(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.toString(), exception.getMessage());
    }

    @Test
    public void testActivateAccountByAccountNumber_AccountAlreadyActivatedException() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber()).equals(AccountStatus.ACTIVATED)).thenThrow(new AccountAlreadyActivatedException(ErrorsForAccount.ACCOUNT_ALREADY_ACTIVATE));
        AccountAlreadyActivatedException exception = assertThrows(AccountAlreadyActivatedException.class, () -> accountServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber()));
        assertEquals(ErrorsForAccount.ACCOUNT_ALREADY_ACTIVATE.toString(), exception.getMessage());
    }

    @Test
    public void testDeactivateAccountByAccountNumber() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
        deactivatedAccount = account;
        deactivatedAccount.setAccountType(AccountStatus.DEACTIVATED.name());
        when(bankAccountRepository.save(deactivatedAccount)).thenReturn(deactivatedAccount);

        DeactivateAccountResponse actual = accountServiceImplementation.deactivateAccountByAccountNumber(account.getAccountNumber());
        DeactivateAccountResponse expected = new DeactivateAccountResponse(true, ForAccount.DEACTIVATE_ACCOUNT.getMessage());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeactivateAccountByAccountNumber_AccountNotAvailableException() {
        when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenThrow(new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE));
        AccountNotAvailableException exception = assertThrows(AccountNotAvailableException.class, () -> accountServiceImplementation.deactivateAccountByAccountNumber(account.getAccountNumber()));
        assertEquals(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.toString(), exception.getMessage());
    }

    @Test
    public void testGetAllDeactivatedAccounts() {

        deactivatedAccount = account;
        deactivatedAccount.setAccountType(AccountStatus.DEACTIVATED.name());

        //for searching deactivated accounts list
        deactivatedAccounts.add(deactivatedAccount);

        when(bankAccountRepository.findDeactivatedAccounts()).thenReturn(deactivatedAccounts);
        DeactivatedAccountsResponse actual = accountServiceImplementation.getAllDeactivatedAccounts();
        DeactivatedAccountsResponse expected = new DeactivatedAccountsResponse(true, ForAccount.LIST_OF_DEACTIVATED_ACCOUNTS.getMessage(), deactivatedAccounts);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllDeactivatedAccounts_AccountNotAvailableException() throws AccountNotAvailableException {
        when(bankAccountRepository.findDeactivatedAccounts()).thenReturn(deactivatedAccounts);
        when(bankAccountRepository.findDeactivatedAccounts()).thenThrow(new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE));

        Exception exception = assertThrows(AccountNotAvailableException.class, () -> accountServiceImplementation.getAllDeactivatedAccounts());
        assertEquals(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.toString(), exception.getMessage());
    }

    @Test
    public void testGetAccountBalance() {

        Long accountNumber = account.getAccountNumber();
        when(bankAccountRepository.findByAccountNumberEquals(accountNumber)).thenReturn(account);

        BalanceInquiryResponse actual = accountServiceImplementation.getAccountBalance(accountNumber);
        BalanceInquiryResponse expected = new BalanceInquiryResponse(true, ForAccount.AVAILABLE_BALANCE.getMessage() + account.getBalance());

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAccountBalance_AccountNotAvailableException() {
        Long accountNumber = account.getAccountNumber();
        when(bankAccountRepository.findByAccountNumberEquals(accountNumber)).thenReturn(account);
        when(bankAccountRepository.findByAccountNumberEquals(accountNumber)).thenThrow(new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE));

        Exception exception = assertThrows(AccountNotAvailableException.class, () -> accountServiceImplementation.getAccountBalance(accountNumber));

        String expected = ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.toString();
        String actual = exception.getMessage();
        assertEquals(expected, actual);

    }


}
