package com.cognologix.bankingApplication.dto.Responses.CustomerOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.dto.TransactionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransactionStatementResponse extends BaseResponse {
    List<TransactionDto> bankTransactions;

    public TransactionStatementResponse(Boolean success, List<TransactionDto> bankTransactions) {
        super(success);
        this.bankTransactions = bankTransactions;
    }
}
