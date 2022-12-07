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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@XmlRootElement
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId")
    private Integer accountId;

    @NotEmpty(message = "Please enter status")
    @Column(name = "status")
    private String status;

    @NotEmpty(message = "Please enter account type, cannot put empty Account type")
    @Column(name = "accountType")
    private String accountType;

    @NotNull(message = "Account number cannot null")
    @Column(name = "accountNumber")
    private Long accountNumber;

    @NotNull(message = "Balance cannot null")
    @Column(name = "balance")
    private Double balance;

    @OneToOne
    private Customer customer;

}
