package com.cognologix.bankingApplication.dto.entityToDto;

import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class TransactionToTransactionDto {
    private Integer transactionId;
    private String operation;
    private Double amount;
    private LocalDateTime dateOfTransaction;

    public TransactionDto entityToDto(BankTransaction bankTransaction){
        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setTransactionId(bankTransaction.getTransactionId());
        transactionDto.setOperation(bankTransaction.getOperation());
        transactionDto.setAmount(bankTransaction.getAmount());
        transactionDto.setDateOfTransaction(bankTransaction.getDateOfTransaction());
        return transactionDto;
    }
}
