package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.services.BankOperationsService;
import com.cognologix.bankingApplication.services.CustomerOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/customer")
public class CustomerServiceController {

    //customer related operations
    @Autowired
    CustomerOperationService customerOperationService;

    @Autowired
    BankOperationsService bankOperationsService;

    //create and return created customer by giving parameter to customer
    @PostMapping(value = "/createCustomer",
            consumes = {"application/json", "application/xml"})
    public ResponseEntity<CreateCustomerResponse> createNewAccount(@Valid @RequestBody Customer customer) {
        CreateCustomerResponse createCustomerResponse = customerOperationService.createNewCustomer(customer);
        HttpStatus httpStatus = createCustomerResponse.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(createCustomerResponse, httpStatus);
    }

    //checking balance by giving account number
    @GetMapping("/balanceInquiry")
    public ResponseEntity<BalanceInquiryResponse> checkBalance(@Valid @PathParam(value = "accountNumber") Long accountNumber) {
        BalanceInquiryResponse balanceInquiryResponse = customerOperationService.getAccountBalance(accountNumber);
        HttpStatus httpStatus = balanceInquiryResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(balanceInquiryResponse, httpStatus);
    }

    //get list of BankTransaction details
    @GetMapping("/statementOfTransaction")
    public ResponseEntity<TransactionStatementResponse> getStatementByAccountNumber(@PathParam("accountNumber") Long accountNumber) {
        TransactionStatementResponse transactionStatementResponse = bankOperationsService.transactionsOfAccount(accountNumber);
        HttpStatus httpStatus = transactionStatementResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(transactionStatementResponse, httpStatus);
    }

    //updating the customer information
    @PatchMapping("/updateCustomer")
    public ResponseEntity<CustomerUpdateResponse> updateInformationOfCustomer(@RequestBody Customer customer) {
        CustomerUpdateResponse customerUpdateResponse = customerOperationService.updateCustomer(customer);
        HttpStatus httpStatus = customerUpdateResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(customerUpdateResponse, httpStatus);
    }

    //returning all customers which is saved in database
    @GetMapping("/getAllCustomers")
    public ResponseEntity<GetAllCustomerResponse> getAllCustomers() {
        GetAllCustomerResponse getAllCustomerResponse = customerOperationService.getAllCustomers();
        HttpStatus httpStatus = getAllCustomerResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(getAllCustomerResponse, httpStatus);
    }


}