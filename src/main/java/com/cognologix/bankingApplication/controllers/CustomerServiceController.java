package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllAccountsForCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.services.BankOperationsService;
import com.cognologix.bankingApplication.services.CustomerOperationService;
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
public class CustomerServiceController {

    //customer related operations
    @Autowired
    private CustomerOperationService customerOperationService;

    @Autowired
    private BankOperationsService bankOperationsService;

    //create and return created customer by giving parameter to customer
    @PostMapping(value = "/create")
    public ResponseEntity<CreateCustomerResponse> createNewAccount(@Valid @RequestBody CustomerDto customerDto) {
        final CreateCustomerResponse createCustomerResponse = customerOperationService.createNewCustomer(customerDto);
        HttpStatus httpStatus = createCustomerResponse.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(createCustomerResponse, httpStatus);
    }

    //get list of BankTransaction details
    @GetMapping("/statement-of-transaction")
    public ResponseEntity<TransactionStatementResponse> getStatementByAccountNumber(@PathParam("accountNumber") Long accountNumber) {
        final TransactionStatementResponse transactionStatementResponse = bankOperationsService.transactionsOfAccount(accountNumber);
        HttpStatus httpStatus = transactionStatementResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(transactionStatementResponse, httpStatus);
    }

    //updating the customer information
    @PatchMapping("/update")
    public ResponseEntity<CustomerUpdateResponse> updateInformationOfCustomer(@RequestBody CustomerDto customerDto) {
        final CustomerUpdateResponse customerUpdateResponse = customerOperationService.updateCustomer(customerDto);
        HttpStatus httpStatus = customerUpdateResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(customerUpdateResponse, httpStatus);
    }

    //returning all customers which is saved in database
    @GetMapping("/get-all-customers")
    public ResponseEntity<GetAllCustomerResponse> getAllCustomers() {
        final GetAllCustomerResponse getAllCustomerResponse = customerOperationService.getAllCustomers();
        HttpStatus httpStatus = getAllCustomerResponse.getSuccess() ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(getAllCustomerResponse, httpStatus);
    }

    @GetMapping("/get-accounts/{customerId}")
    public ResponseEntity<GetAllAccountsForCustomerResponse> getAllAccountsByAdharNumber(@PathVariable Integer customerId){
        GetAllAccountsForCustomerResponse getAllAccountsForCustomer=customerOperationService.getAllAccountsForACustomer(customerId);
        HttpStatus httpStatus = getAllAccountsForCustomer.getSuccess() ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(getAllAccountsForCustomer,httpStatus);
    }


}