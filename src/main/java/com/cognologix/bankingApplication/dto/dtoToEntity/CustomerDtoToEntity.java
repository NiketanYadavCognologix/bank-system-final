package com.cognologix.bankingApplication.dto.dtoToEntity;

import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.entities.Customer;

import java.time.LocalDate;

public class CustomerDtoToEntity {
    private Integer customerId;
    private String customerName;
    private LocalDate dateOfBirth;
    private String adharNumber;
    private String panCardNumber;
    private String emailId;
    private String gender;

    public Customer dtoToEntity(CustomerDto customerDto){
        Customer customer =new Customer();
        customer.setCustomerId(customerDto.getCustomerId());
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setAdharNumber(customerDto.getAdharNumber());
        customer.setPanCardNumber(customerDto.getPanCardNumber());
        customer.setEmailId(customerDto.getEmailId());
        customer.setGender(customerDto.getGender());
        return customer;
    }
}
