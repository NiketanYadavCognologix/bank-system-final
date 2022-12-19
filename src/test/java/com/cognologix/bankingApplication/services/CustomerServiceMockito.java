package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.enums.responseMessages.ForCustomer;
import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.services.implementation.CustomerOperationServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomerServiceMockito.class)
public class CustomerServiceMockito {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private CustomerOperationServiceImplementation customerOperationServiceImplementation;

    CustomerDto customerDto = new CustomerDto(1, "Onkar",
            LocalDate.of(1998, 11, 11), "903997989010",
            "PSURYA", "sury6awanshi@gmail.com", "Male");
    Customer customer = new Customer(1, "Onkar",
            LocalDate.of(1998, 11, 11), "903997989010",
            "PSURYA", "sury6awanshi@gmail.com", "Male");

    Customer customerForUpdating = new Customer(1, "Onkar",
            LocalDate.of(1998, 11, 11), "903997989010",
            "PSURYA", "sury6awanshi@gmail.com", "Male");

    AccountDto accountDto = new AccountDto(3L, "Current", 1000.0, 1);

    Account account = new Account(1L, "Active", accountDto.getAccountType(),
            accountDto.getBalance(), customer);
    List<Customer> customers = new ArrayList<>();

    @Test
    void createNewCustomer() {
        customerRepository.findByCustomerAdharNumberPanCardNumberEmailId(customer.getAdharNumber(),
                customer.getPanCardNumber(), customer.getEmailId());

        when(customerRepository.save(customer)).thenReturn(customer);
        CreateCustomerResponse actual = customerOperationServiceImplementation.createNewCustomer(customerDto);

        CreateCustomerResponse expected = new CreateCustomerResponse(true, ForCustomer.createCustomer.name(), customer);
        assertEquals(expected, actual);
    }

    @Test
    void createNewCustomer_CustomerAlreadyExistException() {
        when(customerRepository.findByCustomerAdharNumberPanCardNumberEmailId(customer.getAdharNumber(),
                customer.getPanCardNumber(), customer.getEmailId())).thenReturn(customer);

        CustomerAlreadyExistException exception = assertThrows(CustomerAlreadyExistException.class, () -> customerOperationServiceImplementation.createNewCustomer(customerDto));

        assertEquals(ErrorsForCustomer.customerAlreadyExist.getMessage(), exception.getMessage());

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
            when(customerRepository.findByCustomerIdEquals(customerDto.getCustomerId())).thenReturn(customer);
            Customer updatedCustomer = new Customer(1, "Niketan",
                    LocalDate.of(1998, 11, 11), "903997989010",
                    "PAK36SURYA", "sury6awanshi@gmail.com", "Male");
            when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);
            CustomerUpdateResponse actual = customerOperationServiceImplementation.updateCustomer(customerDto);
            CustomerUpdateResponse expected = new CustomerUpdateResponse(true, ForCustomer.updateCustomer.name(), updatedCustomer);
            assertEquals(expected, actual);
    }
    @Test
    void updateCustomer_CustomerAlreadyExistException() {
        when(customerRepository.findSimilarToAdharNumberPanCardNumberEmailId(customer.getCustomerId(),customer.getAdharNumber(),
                customer.getPanCardNumber(), customer.getEmailId())).thenReturn(customer);
        when(customerRepository.findByCustomerIdEquals(customer.getCustomerId())).thenReturn(customer);
        CustomerAlreadyExistException exception = assertThrows(CustomerAlreadyExistException.class, () -> customerOperationServiceImplementation.updateCustomer(customerDto));

        assertEquals(ErrorsForCustomer.customerAlreadyExist.getMessage(), exception.getMessage());

    }

    @Test
    void updateCustomer_CustomerNotFoundException() {
        when(customerRepository.findByCustomerIdEquals(customer.getCustomerId())).thenReturn(null);
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> customerOperationServiceImplementation.updateCustomer(customerDto));

        assertEquals(ErrorsForCustomer.customerNotFound.getMessage(), exception.getMessage());

    }
}
