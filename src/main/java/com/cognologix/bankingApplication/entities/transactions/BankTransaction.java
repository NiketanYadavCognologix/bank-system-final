package com.cognologix.bankingApplication.entities.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bankTransaction")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private Integer transactionId;

    @Column(name = "fromAccountNumber")
    private Long fromAccountNumber;

    @Column(name = "toAccountNumber")
    private Long toAccountNumber;

    @Column(name = "operation")
    private String operation;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "dateOfTransaction")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private LocalDateTime dateOfTransaction;
}
