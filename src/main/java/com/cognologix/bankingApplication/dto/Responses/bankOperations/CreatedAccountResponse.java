package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.entities.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedAccountResponse extends BaseResponse {
    CreateAccountResponseDto createAccountResponseDto;

    public CreatedAccountResponse(Boolean success, String message, CreateAccountResponseDto createAccountResponseDto) {
        super(success);
        this.setMessage(message);
        this.createAccountResponseDto = createAccountResponseDto;
    }

}
