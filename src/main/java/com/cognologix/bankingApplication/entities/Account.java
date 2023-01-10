package com.cognologix.bankingApplication.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountNumber")
    private Long accountNumber;

    @Column(name = "bank")
    private String bankName;

    @Column(name = "branch")
    private String branch;

    @Column(name = "ifscCode")
    private String IFSCCode;

    @Column(name = "status")
    private String status;

    @Column(name = "accountType")
    private String accountType;

    @Column(name = "balance")
    private Double balance;



    @OneToOne
    private Customer customer;

}
