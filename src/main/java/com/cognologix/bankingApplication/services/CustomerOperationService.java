package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllAccountsForCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import org.springframework.stereotype.Service;

@Service
public interface CustomerOperationService {
    CreateCustomerResponse createNewCustomer(CustomerDto customerDto);
    GetAllCustomerResponse getAllCustomers();

    GetAllAccountsForCustomerResponse getAllAccountsForACustomer(Integer customerId);
    CustomerUpdateResponse updateCustomer(CustomerDto customerDto);
}
