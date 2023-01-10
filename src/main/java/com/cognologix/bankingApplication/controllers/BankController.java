package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.BranchDto;
import com.cognologix.bankingApplication.dto.Responses.BankOperations.CreateBranchResponse;
import com.cognologix.bankingApplication.enums.LogMessages;
import com.cognologix.bankingApplication.services.BankService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class BankController {

    private static final Logger LOGGER = LogManager.getLogger(BankController.class);
    @Autowired
    private BankService bankService;

    @PostMapping("/create-branch")
    public ResponseEntity<CreateBranchResponse> createNewBranch(@RequestBody BranchDto branchDto){
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        CreateBranchResponse createBranchResponse =bankService.createBranch(branchDto);
        HttpStatus httpStatus = createBranchResponse.getSuccess()?HttpStatus.CREATED:HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(createBranchResponse,httpStatus);
    }
}
