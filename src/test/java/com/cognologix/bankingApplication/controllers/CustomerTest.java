package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllAccountsForCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.enums.responseMessages.ForCustomer;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerTest extends AbstractTest {


    Customer customer = new Customer(1, "Onkar", "11-11-1998",
            "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");
    AccountDto accountDto = new AccountDto(null, "Savings", 1000.0, 1);

    Account account = new Account(accountDto.getAccountNumber(),
            "Activate", accountDto.getAccountType(), accountDto.getBalance(), customer);

    @Test
    @DisplayName("get all customers")
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
    @DisplayName("get statement of transaction")
    public void testStatementOfTransactions() throws Exception {
        this.setUp();
        String uri = "/customer/statement-of-transaction";
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).param("accountNumber", mapToJson(1L))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();
        TransactionStatementResponse transactions = super.mapFromJson(actualResult, TransactionStatementResponse.class);

        assertTrue(transactions.getBankTransactions().size() > 0);
    }

    @Test
    @DisplayName("create customer")
    public void testCreateCustomer() throws Exception {
        this.setUp();

        Customer customer = new Customer(5, "Onkar", "11-11-1998",
                "973998979410", "PA7p7SURYA", "sury87awanshi@gmail.com", "Male");

        CustomerDto customerDto = new CustomerDto(5, "Onkar", "11-11-1998",
                "973998979410", "PA7p7SURYA", "sury87awanshi@gmail.com", "Male");


        String uri = "/customer/create";
        CreateCustomerResponse expected = new CreateCustomerResponse(true, ForCustomer.CREATE_CUSTOMER.getMessage(), customer);
        String inputInJson = this.mapToJson(expected);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(customerDto)))
                .andExpect(status().isCreated())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(inputInJson, content);
    }

    @Test
    @DisplayName("customer already exist")
    public void testCreateCustomer_CustomerAlreadyExistException() throws Exception {
        this.setUp();

        Customer customer = new Customer(4, "Onkar", "11-11-1998",
                "973998989410", "PANp7SURYA", "sury8awanshi@gmail.com", "Male");

        CustomerDto customerDto = new CustomerDto(4, "Onkar", "11-11-1998",
                "973998989410", "PANp7SURYA", "sury8awanshi@gmail.com", "Male");


        String uri = "/customer/create";
        this.mvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(customerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomerAlreadyExistException))
                .andExpect(result -> assertEquals(ErrorsForCustomer.CUSTOMER_ALREADY_EXIST.toString(),result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("update customer")
    public void testUpdateCustomer() throws Exception {
        this.setUp();
        Customer customer = new Customer(3, "Onkar", "11-11-1998",
                "903998989410", "PAN37SURYA", "sury9awanshi@gmail.com", "Male");
        CustomerDto customerDto = new CustomerDto(3, "Onkar", "11-11-1998",
                "903998989410", "PAN37SURYA", "sury9awanshi@gmail.com", "Male");

        String uri = "/customer/update";
        CustomerUpdateResponse expected = new CustomerUpdateResponse(true, ForCustomer.UPDATE_CUSTOMER.getMessage(), customer);
        String inputInJson = this.mapToJson(expected);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.patch(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(customerDto)))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(inputInJson, content);
    }

    @Test
    @DisplayName("customer not found to update")
    public void testUpdateCustomer_CustomerNotFoundException() throws Exception {
        this.setUp();
        CustomerDto customerDto = new CustomerDto(19, "Onkar", "11-11-1998",
                "903998989410", "PAN37SURYA", "sury9awanshi@gmail.com", "Male");

        String uri = "/customer/update";
        this.mvc.perform(MockMvcRequestBuilders.patch(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(customerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomerNotFoundException))
                .andExpect(result -> assertEquals(ErrorsForCustomer.CUSTOMER_NOT_FOUND.toString(),result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("credentials already exist for other account")
    public void testUpdateCustomer_CustomerAlreadyExistException() throws Exception {
        this.setUp();
        CustomerDto customerDto = new CustomerDto(4, "Onkar", "11-11-1998",
                "903998989410", "PAN37SURYA", "sury9awanshi@gmail.com", "Male");

        String uri = "/customer/update";
        this.mvc.perform(MockMvcRequestBuilders.patch(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(customerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomerAlreadyExistException))
                .andExpect(result -> assertEquals(ErrorsForCustomer.CUSTOMER_ALREADY_EXIST.toString(),result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("all account for customer")
    public void testGetAllAccountsForCustomer() throws Exception {
        this.setUp();
        String uri = "/customer/get-accounts/1";
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        GetAllAccountsForCustomerResponse response = super.mapFromJson(content, GetAllAccountsForCustomerResponse.class);
        assertEquals(302, status);
        assertTrue(response.getAccountsInResponse().size() > 0);
    }

    @Test
    @DisplayName("Account not available for given customer")
    public void testGetAllAccountsForCustomer_AccountNotAvailableException() throws Exception {
        this.setUp();
        String uri = "/customer/get-accounts/2";
        this.mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccountNotAvailableException))
                .andExpect(result -> assertEquals(ErrorsForCustomer.ACCOUNT_NOT_AVAILABLE.toString(),result.getResolvedException().getMessage()));
    }

}
