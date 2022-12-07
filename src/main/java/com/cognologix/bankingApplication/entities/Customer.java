package com.cognologix.bankingApplication.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerId")
	private Integer customerId;

	@Column(name = "customerName")
	@NotEmpty(message = "Account holder name cannot blank")
	private String customerName;

	@Column(name = "dateOfBirth")
	@NotNull(message = "Date of birth cannot blank")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@Column(name = "adharNumber",unique = true)
	@NotEmpty(message = "Adhar number cannot blank")
	@Size(min = 12, max = 12, message = "Adhar number should be 12 character")
	private String adharNumber;

	@Column(name = "panCardNumber",unique = true)
	@NotEmpty(message = "PAN number cannot blank")
	@Size(min = 10, max = 10, message = "Adhar number should be 10 character...")
	private String panCardNumber;

	@Column(name = "emailId",unique = true)
	@Email(message = "Please enter valid email id....")
	private String emailId;

	@Column(name = "gender")
	@NotEmpty(message = "Gender cannot blank")
	private String gender;

	@Override
	public String toString() {
		String messageBody = "CustomerId={0} | customerName={1} | dateOfBirth={2} | " +
				"adharNumber={3} | PanNumber={4} | EmailId={5} | gender={6}";
		return java.text.MessageFormat.format(messageBody,customerId, customerName,
				dateOfBirth, adharNumber,panCardNumber,emailId,gender);
	}

}
