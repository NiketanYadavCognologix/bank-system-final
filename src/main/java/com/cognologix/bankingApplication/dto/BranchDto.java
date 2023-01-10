package com.cognologix.bankingApplication.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {

    @NotEmpty(message = "branch name cannot null")
    private String branchName;

    @NotEmpty(message = "address are not empty")
    private String address;

    @NotEmpty(message = "bankName are not empty")
    private String bankName;
}
