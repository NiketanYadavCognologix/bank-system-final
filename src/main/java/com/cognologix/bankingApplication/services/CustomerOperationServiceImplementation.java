package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.globleObjectLists.DataSouceForCustomer;
import com.cognologix.bankingApplication.globleObjectLists.DataSoucrce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOperationServiceImplementation implements CustomerOperationService {
    @Autowired
    private DataSouceForCustomer dataSouceForCustomer;

    @Autowired
    private DataSoucrce dataSoucrce;

    @Override
    public Customer createNewCustomer(CustomerDto customerDto) {
        return dataSouceForCustomer.saveCustomer(customerDto);
    }

    @Override
    public Double getAccountBalance(Long accountNumber) {
        return dataSoucrce.getCurrentBalance(accountNumber);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return dataSouceForCustomer.getAllCustomers();
    }


}
