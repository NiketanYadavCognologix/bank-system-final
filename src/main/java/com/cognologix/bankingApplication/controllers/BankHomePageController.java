package com.cognologix.bankingApplication.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class BankHomePageController {

    public ResponseEntity<String> homePageMassage(){
        return new ResponseEntity<>("Welcome to Cognologix banking application", HttpStatus.OK);
    }
}
