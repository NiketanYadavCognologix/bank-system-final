package com.cognologix.bankingApplication.services.implementation;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllAccountsForCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.dto.dtoToEntity.CustomerDtoToEntity;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.enums.LogMessages;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.enums.responseMessages.ForCustomer;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.services.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImplementation implements CustomerService {

    private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImplementation.class);
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    //creating new customer
    @Override
    public CreateCustomerResponse createNewCustomer(CustomerDto customerDto) {
        //if the customer identity element is already exist then throws exception
        try {
            Customer customer = new CustomerDtoToEntity().dtoToEntity(customerDto);
            Customer availableCustomer = customerRepository.findByCustomerAdharNumberPanCardNumberEmailId(customer.getAdharNumber(),
                    customer.getPanCardNumber(), customer.getEmailId());
            if (null != availableCustomer) {
                throw new CustomerAlreadyExistException(ErrorsForCustomer.CUSTOMER_ALREADY_EXIST);
            }

            Customer customerCreated = customerRepository.save(customer);
            ThreadContext.put("executionStep","step-2");
            LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());

            ThreadContext.put("executionStep","step-3");
            LOGGER.info(ForCustomer.CREATE_CUSTOMER.getMessage());
            return new CreateCustomerResponse(true, ForCustomer.CREATE_CUSTOMER.getMessage(), customerCreated);

        } catch (CustomerAlreadyExistException exception) {
            throw new CustomerAlreadyExistException(exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    //returns all the customers
    @Override
    public GetAllCustomerResponse getAllCustomers() {
        GetAllCustomerResponse getAllCustomerResponse = new GetAllCustomerResponse(true, customerRepository.findAll());
        return getAllCustomerResponse;
    }

    @Override
    public GetAllAccountsForCustomerResponse getAllAccountsForACustomer(Integer customerId) {
        List<Account> matchingAccounts = bankAccountRepository.getAllAccountsForCustomer(customerId);
        if (matchingAccounts.isEmpty()) {
            throw new AccountNotAvailableException(ErrorsForCustomer.ACCOUNT_NOT_AVAILABLE);
        }
        ThreadContext.put("executionStep","step-2");
        LOGGER.info(ForCustomer.ALL_ACCOUNTS_FOR_CUSTOMER.getMessage());
        return new GetAllAccountsForCustomerResponse(true, ForCustomer.ALL_ACCOUNTS_FOR_CUSTOMER.getMessage(), matchingAccounts);
    }

    //update the customer
    @Override
    public CustomerUpdateResponse updateCustomer(CustomerDto customerDto) {
        try {
            Customer customer = new CustomerDtoToEntity().dtoToEntity(customerDto);

            Customer customerFound = customerRepository.findByCustomerIdEquals(customer.getCustomerId());
            if (customerFound == null) {
                throw new CustomerNotFoundException(ErrorsForCustomer.CUSTOMER_NOT_FOUND);
            }
            Customer existingOtherCustomer = customerRepository.findSimilarToAdharNumberPanCardNumberEmailId(customerDto.getCustomerId(),
                    customerDto.getAdharNumber(), customerDto.getPanCardNumber(), customerDto.getEmailId());
            if (null != existingOtherCustomer) {
                throw new CustomerAlreadyExistException(ErrorsForCustomer.CUSTOMER_ALREADY_EXIST);
            }
            Customer updatedCustomer = customerRepository.save(customer);
            ThreadContext.put("executionStep","step-2");
            LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());

            CustomerUpdateResponse customerUpdateResponse = new CustomerUpdateResponse(true,
                    ForCustomer.UPDATE_CUSTOMER.getMessage(), updatedCustomer);

            ThreadContext.put("executionStep","step-3");
            LOGGER.info(ForCustomer.UPDATE_CUSTOMER.getMessage());
            return customerUpdateResponse;
        } catch (CustomerNotFoundException exception) {
            throw new CustomerNotFoundException(exception.getMessage());
        } catch (CustomerAlreadyExistException exception) {
            throw new CustomerAlreadyExistException(exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


}
