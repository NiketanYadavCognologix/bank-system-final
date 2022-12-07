package com.cognologix.bankingApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

//this account dto is taken by bank and return created account of customer
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class AccountDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountID;

    @NotEmpty(message = "Type of account cannot null")
    private String accountType;


    @NotNull(message = "Balance cannot null")
    private Double balance;

    @NotNull(message = "Customer cannot blank")
    private Integer customerId;

}
