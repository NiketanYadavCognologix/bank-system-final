package com.cognologix.bankingApplication.entities.banks;

import com.cognologix.bankingApplication.entities.banks.branch.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bankId;

    @Column(name = "bank")
    @NotEmpty(message = "bank name cannot null")
    private String bankName;

    @OneToMany
    private List<Branch> branches;

}
