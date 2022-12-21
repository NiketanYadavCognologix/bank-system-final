package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.*;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.enums.AccountStatus;
import com.cognologix.bankingApplication.enums.responseMessages.ForAccount;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(AccountController.class)
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AccountTest extends AbstractTest {

    Customer customer = new Customer(1, "Onkar", "11-11-1998",
            "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");

    CustomerDto customerDto = new CustomerDto(1, "Onkar", "11-11-1998",
            "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");
    AccountDto accountDto = new AccountDto(7L, "SAVINGS", 1000.0, 1);

    Account account = new Account(accountDto.getAccountNumber(),
            "ACTIVATED", accountDto.getAccountType(), accountDto.getBalance(), customer);

    @Test
    public void createAccount() throws Exception {
        this.setUp();
        String uri = "/account/create";
        CreatedAccountResponse createdAccountResponse = new CreatedAccountResponse(true,
                ForAccount.CREATE_ACCOUNT.getMessage(), account);
        String jsonRequest = mapToJson(createdAccountResponse);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(mapToJson(accountDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonRequest, result);
    }

    @Test
    public void testDeactivateAccount() throws Exception {
        this.setUp();
        String uri = "/account/deactivate";
        DeactivateAccountResponse deactivateAccountResponse = new DeactivateAccountResponse(true,
                ForAccount.DEACTIVATE_ACCOUNT.getMessage());
        String jsonRequest = mapToJson(deactivateAccountResponse);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("accountNumber", mapToJson(account.getAccountNumber())))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonRequest, result);
    }

    @Test
    public void testGetAllDeactivatedAccounts() throws Exception {
        this.setUp();
        List<Account> deactivateAccounts=new ArrayList<>();
        Account account = new Account(accountDto.getAccountNumber(),
                AccountStatus.DEACTIVATED.name(), accountDto.getAccountType(), accountDto.getBalance(), customer);
        deactivateAccounts.add(account);
        String uri = "/account/get-deactivated-accounts";
        DeactivatedAccountsResponse deactivatedAccountsResponse = new DeactivatedAccountsResponse(true,
                ForAccount.LIST_OF_DEACTIVATED_ACCOUNTS.getMessage(), deactivateAccounts);
        String jsonRequest = mapToJson(deactivatedAccountsResponse);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonRequest, result);
    }
    @Test
    public void testActivateAccount() throws Exception {
        this.setUp();
        String uri = "/account/activate";
        ActivateAccountResponse activateAccountResponse = new ActivateAccountResponse(true,
                ForAccount.ACTIVATED_ACCOUNT.getMessage());
        String jsonRequest = mapToJson(activateAccountResponse);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("accountNumber", mapToJson(account.getAccountNumber())))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonRequest, result);
    }

    @Test
    public void testDepositAmount() throws Exception {
        this.setUp();
        String uri = "/account/deposit-amount";
        Double amount=5000.00;
        DepositAmountResponse expectedResponse = new DepositAmountResponse(true,
                amount+ForAccount.DEPOSIT_AMOUNT.getMessage());
        String jsonRequest = mapToJson(expectedResponse);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("accountNumber", mapToJson(1L))
                        .param("amount",mapToJson(amount)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonRequest, actualResult);
    }

    @Test
    @DisplayName("Positive withdraw amount")
    public void testWithdrawAmount() throws Exception {
        this.setUp();
        String uri = "/account/withdraw-amount";
        Double amount=500.00;
        WithdrawAmountResponse expectedResponse = new WithdrawAmountResponse(true,
                amount+ForAccount.WITHDRAW_AMOUNT.getMessage());
        String jsonRequest = mapToJson(expectedResponse);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("accountNumber", mapToJson(1L))
                        .param("amount",mapToJson(amount)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonRequest, actualResult);
    }

    @Test
    public void testTransferAmount() throws Exception {
        this.setUp();
        String uri = "/account/transfer-amount";
        Double amount=500.00;
        WithdrawAmountResponse expectedResponse = new WithdrawAmountResponse(true,
                amount+ForAccount.TRANSFER_AMOUNT.getMessage()+500.0);
        String jsonRequest = mapToJson(expectedResponse);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("senderAccountNumber", mapToJson(1L))
                        .param("receiverAccountNumber",mapToJson(2L))
                        .param("amount",mapToJson(amount)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonRequest, actualResult);
    }

    @Test
    public void testCheckBalance() throws Exception {
        this.setUp();
        String uri = "/account/balance-inquiry";
        BalanceInquiryResponse balanceInquiryResponse = new BalanceInquiryResponse(true,
                ForAccount.AVAILABLE_BALANCE.getMessage() + account.getBalance());

        String expect = mapToJson(balanceInquiryResponse);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("accountNumber", mapToJson(account.getAccountNumber())))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(expect, result);

    }


}
