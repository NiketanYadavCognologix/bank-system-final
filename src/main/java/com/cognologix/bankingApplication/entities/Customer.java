package com.cognologix.bankingApplication.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerId")
	private Integer customerId;

	@Column(name = "customerName")
	private String customerName;

	@Column(name = "dateOfBirth")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@Column(name = "adharNumber",unique = true)
	private String adharNumber;

	@Column(name = "panCardNumber",unique = true)
	private String panCardNumber;

	@Column(name = "emailId",unique = true)
	private String emailId;

	@Column(name = "gender")
	private String gender;

}
