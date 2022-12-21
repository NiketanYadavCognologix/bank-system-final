package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.*;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.enums.responseMessages.ForAccount;
import com.cognologix.bankingApplication.enums.responseMessages.ForCustomer;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequestMapping("/customer")
public class CustomerTest extends AbstractTest {


    Customer customer = new Customer(1, "Onkar", "11-11-1998",
            "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");

    CustomerDto customerDto ;
    AccountDto accountDto = new AccountDto(null, "Savings", 1000.0, 1);

    Account account = new Account(accountDto.getAccountNumber(),
            "Activate", accountDto.getAccountType(), accountDto.getBalance(), customer);
    @Test
    public void getProductsList() throws Exception {
        this.setUp();
        String uri = "/customer/get-all-customers";
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        //assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        GetAllCustomerResponse getAllCustomerResponse = super.mapFromJson(content, GetAllCustomerResponse.class);
        assertTrue(getAllCustomerResponse.getCustomers().size() > 0);
    }

    @Test
    public void testWithdrawAmount() throws Exception {
        this.setUp();
        String uri = "/customer/statement-of-transaction";
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).param("accountNumber",mapToJson(1L))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();
        TransactionStatementResponse transactions=super.mapFromJson(actualResult,TransactionStatementResponse.class);

        assertTrue(transactions.getBankTransactions().size() > 0);
    }

    @Test
    public void testCreateCustomer() throws Exception {
        this.setUp();

        Customer customer = new Customer(3, "Onkar", "11-11-1998",
                "903998989410", "PAN37SURYA", "sury9awanshi@gmail.com", "Male");

        CustomerDto customerDto = new CustomerDto(3, "Onkar", "11-11-1998",
                "903998989410", "PAN37SURYA", "sury9awanshi@gmail.com", "Male");


        String uri = "/customer/create";
        CreateCustomerResponse expected = new CreateCustomerResponse(true, ForCustomer.CREATE_CUSTOMER.getMessage(),customer);
        String inputInJson = this.mapToJson(expected);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(customerDto)))
                .andExpect(status().isCreated())
                .andReturn();
        String content=mvcResult.getResponse().getContentAsString();
        assertEquals(inputInJson,content);
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        this.setUp();
        Customer customer = new Customer(3, "Onkar", "11-11-1998",
                "903998989410", "PAN37SURYA", "sury9awanshi@gmail.com", "Male");
        CustomerDto customerDto = new CustomerDto(3, "Onkar", "11-11-1998",
                "903998989410", "PAN37SURYA", "sury9awanshi@gmail.com", "Male");

        String uri = "/customer/update";
        CustomerUpdateResponse expected = new CustomerUpdateResponse(true, ForCustomer.UPDATE_CUSTOMER.getMessage(),customer);
        String inputInJson = this.mapToJson(expected);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.patch(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(customerDto)))
                .andExpect(status().isOk())
                .andReturn();
        String content=mvcResult.getResponse().getContentAsString();
        assertEquals(inputInJson,content);
    }

    @Test
    public void getAllAccountsForCustomer() throws Exception {
        this.setUp();
        String uri = "/customer/get-accounts/1";
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        GetAllAccountsForCustomerResponse response = super.mapFromJson(content, GetAllAccountsForCustomerResponse.class);
        assertTrue(response.getAccountsInResponse().size() > 0);
    }

}
