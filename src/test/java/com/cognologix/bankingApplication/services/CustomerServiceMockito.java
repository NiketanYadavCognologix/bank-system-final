package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.services.implementation.CustomerOperationServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomerServiceMockito.class)
public class CustomerServiceMockito {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    BankAccountRepository bankAccountRepository;

    @InjectMocks
    CustomerOperationServiceImplementation customerOperationServiceImplementation;

    Customer customer = new Customer(1, "Onkar", LocalDate.of(1998, 11, 11),
            "903997989010", "PAK36SURYA", "sury6awanshi@gmail.com", "Male");

    AccountDto accountDto = new AccountDto(3, "Current", 1000.0, 1);

    Account account = new Account(accountDto.getAccountID(), "Active", accountDto.getAccountType(),
            1001L, accountDto.getBalance(), customer);
    List<Customer> customers = new ArrayList<>();

    @Test
    void createNewCustomer() {

        when(customerRepository.findAll()).thenReturn((List<Customer>) customers);
        when(customerRepository.save(customer)).thenReturn(customer);
        CreateCustomerResponse actual = customerOperationServiceImplementation.createNewCustomer(customer);

        CreateCustomerResponse expected = new CreateCustomerResponse(true,
                customer.getCustomerName() + " you have register successfully...", customer);
        assertEquals(expected, actual);
    }

    @Test
    void getAccountBalance() {
        Long accountNumber = account.getAccountNumber();
        when(bankAccountRepository.findByAccountNumberEquals(accountNumber)).thenReturn(account);

        BalanceInquiryResponse actual = customerOperationServiceImplementation.getAccountBalance(accountNumber);
        BalanceInquiryResponse expected = new BalanceInquiryResponse(true,
                "Hi " + account.getCustomer().getCustomerName() + " your account balance is : " + account.getBalance());

        assertEquals(expected, actual);
    }

    @Test
    void getAllCustomers() {
        when(customerRepository.findAll()).thenReturn(customers);

        GetAllCustomerResponse actual = customerOperationServiceImplementation.getAllCustomers();
        GetAllCustomerResponse expected = new GetAllCustomerResponse(true, customerRepository.findAll());
        assertEquals(expected, actual);
    }

    @Test
    void updateCustomer() {
        when(customerRepository.findByCustomerIdEquals(customer.getCustomerId())).thenReturn(customer);
        Customer updatedCustomer = new Customer(1, "Niketan", LocalDate.of(1998, 11, 11),
                "903997989010", "PAK36SURYA", "sury6awanshi@gmail.com", "Male");
        when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);
        CustomerUpdateResponse actual = customerOperationServiceImplementation.updateCustomer(updatedCustomer);
        CustomerUpdateResponse expected = new CustomerUpdateResponse(true, updatedCustomer.getCustomerName()
                + " updated successfully...");
        assertEquals(expected, actual);
    }
}
