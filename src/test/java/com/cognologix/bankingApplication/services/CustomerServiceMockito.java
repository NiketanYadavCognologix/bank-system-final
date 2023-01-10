package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.enums.responseMessages.ForCustomer;
import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.services.implementation.CustomerServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomerService.class)
public class CustomerServiceMockito {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImplementation customerOperationServiceImplementation;

    CustomerDto customerDto = new CustomerDto(1, "Onkar",
            "10-10-1998", "903997989010",
            "PSURYA", "sury6awanshi@gmail.com", "Male");
    Customer customer = new Customer(1, "Onkar",
            "10-10-1998", "903997989010",
            "PSURYA", "sury6awanshi@gmail.com", "Male");

    List<Customer> customers = new ArrayList<>();

    @Test
    void createNewCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);
        CreateCustomerResponse actual = customerOperationServiceImplementation.createNewCustomer(customerDto);

        CreateCustomerResponse expected = new CreateCustomerResponse(true, ForCustomer.CREATE_CUSTOMER.getMessage(), customer);
        assertEquals(expected, actual);
    }

    @Test
    void createNewCustomer_CustomerAlreadyExistException() {
        when(customerRepository.findByCustomerAdharNumberPanCardNumberEmailId(customer.getAdharNumber(),
                customer.getPanCardNumber(), customer.getEmailId())).thenReturn(customer);

        CustomerAlreadyExistException exception = assertThrows(CustomerAlreadyExistException.class, () -> customerOperationServiceImplementation.createNewCustomer(customerDto));

        assertEquals(ErrorsForCustomer.CUSTOMER_ALREADY_EXIST.toString(), exception.getMessage());

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
                "10-10-1998", "903997989010",
                "PAK36SURYA", "sury6awanshi@gmail.com", "Male");
        when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);
        CustomerUpdateResponse actual = customerOperationServiceImplementation.updateCustomer(customerDto);
        CustomerUpdateResponse expected = new CustomerUpdateResponse(true, ForCustomer.UPDATE_CUSTOMER.getMessage(), updatedCustomer);
        assertEquals(expected, actual);
    }

    @Test
    void updateCustomer_CustomerAlreadyExistException() {
        when(customerRepository.findSimilarToAdharNumberPanCardNumberEmailId(customer.getCustomerId(), customer.getAdharNumber(),
                customer.getPanCardNumber(), customer.getEmailId())).thenReturn(customer);
        when(customerRepository.findByCustomerIdEquals(customer.getCustomerId())).thenReturn(customer);
        CustomerAlreadyExistException exception = assertThrows(CustomerAlreadyExistException.class, () -> customerOperationServiceImplementation.updateCustomer(customerDto));

        assertEquals(ErrorsForCustomer.CUSTOMER_ALREADY_EXIST.toString(), exception.getMessage());

    }

    @Test
    void updateCustomer_CustomerNotFoundException() {
        when(customerRepository.findByCustomerIdEquals(customer.getCustomerId())).thenReturn(null);
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> customerOperationServiceImplementation.updateCustomer(customerDto));

        assertEquals(ErrorsForCustomer.CUSTOMER_NOT_FOUND.toString(), exception.getMessage());

    }
}
