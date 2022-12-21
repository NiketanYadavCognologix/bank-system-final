package com.cognologix.bankingApplication.entities.transactions;

import com.cognologix.bankingApplication.enums.Transaction;
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
    private String dateOfTransaction;

    public BankTransaction(Long toAccountNumber, Double amount) {
        this.toAccountNumber=toAccountNumber;
        this.amount=amount;
        this.operation=Transaction.DEPOSIT.name();
        this.dateOfTransaction=LocalDateTime.now().toString();

    }
    public BankTransaction( Double amount,Long fromAccountNumber) {
        this.fromAccountNumber=fromAccountNumber;
        this.amount=amount;
        this.operation= Transaction.WITHDRAW.name();
        this.dateOfTransaction=LocalDateTime.now().toString();

    }

    public BankTransaction(Long accountNumberWhoSendMoney, Long accountNumberWhoReceiveMoney, Double amountForTransfer) {
        this.fromAccountNumber=accountNumberWhoSendMoney;
        this.toAccountNumber=accountNumberWhoReceiveMoney;
        this.amount=amountForTransfer;
        this.operation= (Transaction.TRANSFER.name())+" from " + accountNumberWhoSendMoney + " to " + accountNumberWhoReceiveMoney;
        this.dateOfTransaction=LocalDateTime.now().toString();
    }
}
