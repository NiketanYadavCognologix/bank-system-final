package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.entities.Customer;

import java.util.List;

public interface CustomerOperationService {
    Customer createNewCustomer(CustomerDto customer);
    Double getAccountBalance(Long accountNumber);
    List<Customer> getAllCustomers();
}
