package com.cognologix.bankingApplication.services.implementation;

import com.cognologix.bankingApplication.dao.BankBranchRepository;
import com.cognologix.bankingApplication.dao.BankRepository;
import com.cognologix.bankingApplication.dto.BranchDto;
import com.cognologix.bankingApplication.dto.Responses.BankOperations.CreateBranchResponse;
import com.cognologix.bankingApplication.entities.banks.Bank;
import com.cognologix.bankingApplication.entities.banks.branch.Branch;
import com.cognologix.bankingApplication.enums.LogMessages;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForBank;
import com.cognologix.bankingApplication.enums.responseMessages.ForBank;
import com.cognologix.bankingApplication.exceptions.forBank.BankNameNotFoundException;
import com.cognologix.bankingApplication.exceptions.forBank.BranchAlreadyExistException;
import com.cognologix.bankingApplication.services.BankService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImplementation implements BankService {

    private static final Logger LOGGER = LogManager.getLogger(BankServiceImplementation.class);
    @Autowired
    BankBranchRepository bankBranchRepository;
    @Autowired
    private BankRepository bankRepository;
    @Override
    public CreateBranchResponse createBranch(BranchDto branchDto) {
       try {

           Bank existingBank = bankRepository.findByBankNameEquals(branchDto.getBankName());
           if(null == existingBank){
               throw new BankNameNotFoundException(ErrorsForBank.BANK_NAME_NOT_FOUND);
           }
           Branch foundBranch = bankBranchRepository.findByBranchEquals(branchDto.getBranchName());
           if (null != foundBranch) {
               String bankName = bankRepository.findByBankNameEquals(branchDto.getBankName()).getBankName();
               if(branchDto.getBankName().equalsIgnoreCase(bankName)) {
                   throw new BranchAlreadyExistException(ErrorsForBank.BRANCH_ALREADY_EXIST);
               }
           }
           Branch branch = new Branch();

           branch.setBank(existingBank);
           branch.setBranch(branchDto.getBranchName().toUpperCase());
           branch.setAddress(branchDto.getAddress());
           branch.setIFSCCode("IFSC0100" + bankBranchRepository.findAll().size());


           Branch isSave = bankBranchRepository.save(branch);
           if(null != isSave){
               ThreadContext.put("executionStep","step-2");
               LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
           }
           ThreadContext.put("executionStep","step-3");
           LOGGER.info(ForBank.NEW_BRANCH.getMessage());
           return new CreateBranchResponse(true, ForBank.NEW_BRANCH.getMessage());
       }catch (BranchAlreadyExistException exception){
           LOGGER.error(exception.getBranchAlreadyExist().getMessage());
           throw new BranchAlreadyExistException(exception.getMessage());
       }catch (BankNameNotFoundException exception){
           LOGGER.error(exception.getBankNameNotFound().getMessage());
           throw new BankNameNotFoundException(exception.getMessage());
       }
    }
}
