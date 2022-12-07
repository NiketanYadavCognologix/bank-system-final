package com.cognologix.bankingApplication.services.implementation;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.services.CustomerOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomerOperationServiceImplementation implements CustomerOperationService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    //creating new customer
    @Override
    public CreateCustomerResponse createNewCustomer(Customer customer) {
        //if the customer identity element is already exist then throws exception
        try {
            customerRepository.findAll().stream().forEach(customerFromList -> {
                if (customerFromList.getAdharNumber().equals(customer.getAdharNumber())) {
                    throw new CustomerAlreadyExistException("Customer is already exist by same adhar number....");
                } else if (customerFromList.getPanCardNumber().equals(customer.getPanCardNumber())) {
                    throw new CustomerAlreadyExistException("Customer is already exist by same PanCard number....");
                } else if (customerFromList.getEmailId().equals(customer.getEmailId())) {
                    throw new CustomerAlreadyExistException("Customer is already exist by same email....");
                }
            });
            Customer customerCreated = customerRepository.save(customer);
            CreateCustomerResponse createAccountResponse = new CreateCustomerResponse(true,
                    customer.getCustomerName() + " you have register successfully..."
                    , customerCreated);
            return createAccountResponse;
        } catch (CustomerAlreadyExistException exception) {
            throw new CustomerAlreadyExistException(exception.getMessage());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    //get account balance by account number
    @Override
    public BalanceInquiryResponse getAccountBalance(Long accountNumber) {
        try {
            Account accountAvailable = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (accountAvailable == null) {
                throw new AccountNotAvailableException("Account for given account number does not exist...");
            }
            Double availableBalance = bankAccountRepository.findByAccountNumberEquals(accountNumber).getBalance();
            BalanceInquiryResponse balanceInquiryResponse = new BalanceInquiryResponse(true,
                    "Hi " + accountAvailable.getCustomer().getCustomerName() + " your account balance is : " + availableBalance);
            return balanceInquiryResponse;
        } catch (AccountNotAvailableException exception) {
            throw new AccountNotAvailableException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }

    }

    //returns all the customers
    @Override
    public GetAllCustomerResponse getAllCustomers() {
        GetAllCustomerResponse getAllCustomerResponse = new GetAllCustomerResponse(true, customerRepository.findAll());
        return getAllCustomerResponse;
    }

    //update the customer
    @Override
    public CustomerUpdateResponse updateCustomer(Customer customer) {
        try {
            Customer customerFound = customerRepository.findByCustomerIdEquals(customer.getCustomerId());
            if (customerFound == null) {
                throw new CustomerNotFoundException("Given id customer is not available....");
            }
            customerRepository.findAll().stream()
                    .filter(customerFromCustomerList -> customerFromCustomerList.getCustomerId() != customer.getCustomerId())
                    .collect(Collectors.toList())
                    .stream()
                    .forEach(nonMatchingCustomer -> {
                        if (nonMatchingCustomer.getAdharNumber().equals(customer.getAdharNumber())) {
                            throw new CustomerAlreadyExistException("Error : customer you are going to update have adhar number which is already exist....\nPlease enter another adhar number");
                        } else if (nonMatchingCustomer.getPanCardNumber().equals(customer.getPanCardNumber())) {
                            throw new CustomerAlreadyExistException("Error : customer you are going to update have pan card number which is already exist....\nPlease enter another pan number");
                        } else if (nonMatchingCustomer.getEmailId().equals(customer.getEmailId())) {
                            throw new CustomerAlreadyExistException("Error : customer you are going to update have same email id which is already exist....\nPlease enter another email id");
                        }
                    });

            customerRepository.save(customer);
            CustomerUpdateResponse customerUpdateResponse = new CustomerUpdateResponse(true, customer.getCustomerName()
                    + " updated successfully...");
            return customerUpdateResponse;
        }catch (CustomerAlreadyExistException exception){
            exception.printStackTrace();
            throw new CustomerAlreadyExistException(exception.getMessage());
        }catch (CustomerNotFoundException exception) {
            exception.printStackTrace();
            throw new CustomerNotFoundException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }


}
