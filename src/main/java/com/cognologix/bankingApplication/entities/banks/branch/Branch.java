package com.cognologix.bankingApplication.entities.banks.branch;

import com.cognologix.bankingApplication.entities.banks.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer branchId;

    @Column(name = "branch")
    private String branch;

    @Column(name = "address")
    private String address;

    @Column(name = "ifscCode")
    private String IFSCCode;

    @ManyToOne
    private Bank bank;
}
