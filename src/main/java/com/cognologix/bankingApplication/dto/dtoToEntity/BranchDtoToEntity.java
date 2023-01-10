package com.cognologix.bankingApplication.dto.dtoToEntity;

import com.cognologix.bankingApplication.dto.BranchDto;
import com.cognologix.bankingApplication.entities.banks.branch.Branch;

import java.util.List;


public class BranchDtoToEntity {
     public Branch branchDtoToBranchEntity(BranchDto branchDto, List<Branch> foundBranches) {

        Branch branch = new Branch();

        branch.setBankName(branchDto.getBankName());
        branch.setBranch(branchDto.getBranchName().toUpperCase());
        branch.setAddress(branchDto.getAddress());
        branch.setIFSCCode(branchDto.getBankName().toUpperCase() + "0100" + foundBranches.size());
        return branch;
    }
}
