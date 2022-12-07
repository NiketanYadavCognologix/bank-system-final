package com.cognologix.bankingApplication.dto.Responses;

import lombok.Data;
import java.io.Serializable;

@Data
public class BaseResponse implements Serializable {
    private String message;
    private Boolean success;

    public BaseResponse(Boolean success) {
        this.success = success;
    }
}
