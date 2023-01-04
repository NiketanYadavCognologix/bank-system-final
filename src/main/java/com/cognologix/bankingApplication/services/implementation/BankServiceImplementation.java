package com.cognologix.bankingApplication.services.implementation;

import com.cognologix.bankingApplication.dao.BranchRepository;
import com.cognologix.bankingApplication.dao.BankRepository;
import com.cognologix.bankingApplication.dto.BranchDto;
import com.cognologix.bankingApplication.dto.Responses.BankOperations.CreateBranchResponse;
import com.cognologix.bankingApplication.dto.dtoToEntity.BankDtoToEntity;
import com.cognologix.bankingApplication.dto.dtoToEntity.BranchDtoToEntity;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankServiceImplementation implements BankService {

    private static final Logger LOGGER = LogManager.getLogger(BankServiceImplementation.class);
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private BankRepository bankRepository;
    @Override
    public CreateBranchResponse createBranch(BranchDto branchDto) {
       try {
            //check bank exist or not
           Bank existingBank = bankRepository.findByBankNameEquals(branchDto.getBankName());
           if(null == existingBank){
               throw new BankNameNotFoundException(ErrorsForBank.BANK_NAME_NOT_FOUND);
           }
           //getting list of branches for given Bank
          List<Branch> foundBranches = branchRepository.findByBankNameEquals(existingBank.getBankName());

           //check is branch available for given bank
           List<Branch> isBranchAlreadyAvailable = foundBranches.stream()
                   .filter(branch -> branch.getBranch().equalsIgnoreCase(branchDto.getBranchName()))
                   .collect(Collectors.toList());
           if(!isBranchAlreadyAvailable.isEmpty()) {
                   throw new BranchAlreadyExistException(ErrorsForBank.BRANCH_ALREADY_EXIST);
           }
           //converting branchDto to branch entity
           //argument for generating ifsc code for branch
           Branch branch=new BranchDtoToEntity().branchDtoToBranchEntity(branchDto,foundBranches);

            //adding the branch into banks branch list
           foundBranches.add(branch);

            //save to database
           Branch isSave = branchRepository.save(branch);
           if(null != isSave){
               ThreadContext.put("executionStep","step-2");
               LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
           }
            //creating bank by giving parameters
           Bank bank = new BankDtoToEntity().bankDtoBankEntity(existingBank,foundBranches);

           //saving the updated bank to database
           bankRepository.save(bank);

            //log message wih steps
           ThreadContext.put("executionStep","step-3");
           LOGGER.info(ForBank.NEW_BRANCH.getMessage());
           return new CreateBranchResponse(true, ForBank.NEW_BRANCH.getMessage());
       }catch (BranchAlreadyExistException exception){
           throw new BranchAlreadyExistException(exception.getMessage());
       }catch (BankNameNotFoundException exception){
           throw new BankNameNotFoundException(exception.getMessage());
       }
    }
}
