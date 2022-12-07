package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.entities.Customer;

public interface CustomerOperationService {
    CreateCustomerResponse createNewCustomer(Customer customer);
    BalanceInquiryResponse getAccountBalance(Long accountNumber);
    GetAllCustomerResponse getAllCustomers();

    CustomerUpdateResponse updateCustomer(Customer customer);
}
