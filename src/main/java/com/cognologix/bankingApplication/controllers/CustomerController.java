package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllAccountsForCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.enums.LogMessages;
import com.cognologix.bankingApplication.services.AccountService;
import com.cognologix.bankingApplication.services.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger LOGGER = LogManager.getLogger(CustomerController.class);
    //customer related operations
    @Autowired
    private CustomerService customerOperationService;

    @Autowired
    private AccountService bankOperationsService;

    //create and return created customer by giving parameter to customer
    @PostMapping(value = "/create")
    public ResponseEntity<CreateCustomerResponse> createNewAccount(@Valid @RequestBody CustomerDto customerDto) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final CreateCustomerResponse createCustomerResponse = customerOperationService.createNewCustomer(customerDto);
        HttpStatus httpStatus = createCustomerResponse.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(createCustomerResponse, httpStatus);
    }

    //get list of BankTransaction details
    @GetMapping("/statement-of-transaction")
    public ResponseEntity<TransactionStatementResponse> getStatementByAccountNumber(@PathParam("accountNumber") Long accountNumber) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final TransactionStatementResponse transactionStatementResponse = bankOperationsService.transactionsOfAccount(accountNumber);
        HttpStatus httpStatus = transactionStatementResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(transactionStatementResponse, httpStatus);
    }

    //updating the customer information
    @PatchMapping("/update")
    public ResponseEntity<CustomerUpdateResponse> updateInformationOfCustomer(@RequestBody CustomerDto customerDto) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final CustomerUpdateResponse customerUpdateResponse = customerOperationService.updateCustomer(customerDto);
        HttpStatus httpStatus = customerUpdateResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(customerUpdateResponse, httpStatus);
    }

    //returning all customers which is saved in database
    @GetMapping("/get-all-customers")
    public ResponseEntity<GetAllCustomerResponse> getAllCustomers() {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        GetAllCustomerResponse getAllCustomerResponse = customerOperationService.getAllCustomers();
        HttpStatus httpStatus = getAllCustomerResponse.getSuccess() ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(getAllCustomerResponse, httpStatus);
    }

    @GetMapping("/get-accounts/{customerId}")
    public ResponseEntity<GetAllAccountsForCustomerResponse> getAllAccountsByAdharNumber(@PathVariable Integer customerId){
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        GetAllAccountsForCustomerResponse getAllAccountsForCustomer=customerOperationService.getAllAccountsForACustomer(customerId);
        HttpStatus httpStatus = getAllAccountsForCustomer.getSuccess() ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        ThreadContext.put("executionStep","step-3");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(getAllAccountsForCustomer,httpStatus);
    }


}