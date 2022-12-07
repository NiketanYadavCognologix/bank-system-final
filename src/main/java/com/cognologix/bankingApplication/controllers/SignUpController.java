package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.services.BankOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signUp")
public class SignUpController {
    @Autowired
    private BankOperationsService bankOperationsSevice;

//    @PostMapping(value = "/newAccount",
//            consumes = { "application/json", "application/xml" },
//            produces = {"application/json", "application/xml" })
//    public ResponseEntity<?> createAccount(@RequestBody Customer customer) {
//        JSONObject signUpResult=new JSONObject();
//        bankOperationsSevice.createAccount(customer);
//        signUpResult.put("Account created successfully...",customer);
//        System.out.println(signUpResult);
//        return new ResponseEntity<JSONObject>(signUpResult, HttpStatus.CREATED);
//    }


}
