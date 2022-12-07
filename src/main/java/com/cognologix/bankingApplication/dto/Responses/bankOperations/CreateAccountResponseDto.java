package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponseDto {
    private String customerName;
    private String accountType;
    private Long accountNumber;
    private String status;
    private Double balance;

}
