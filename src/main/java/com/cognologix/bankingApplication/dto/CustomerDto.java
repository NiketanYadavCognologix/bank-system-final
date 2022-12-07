package com.cognologix.bankingApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    //not blank validation for string
    @NotBlank(message = "Account holder name cannot blank")
    private String customerName;

    @NotEmpty(message = "Date of birth cannot blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "Adhar number cannot blank")
    @Size(min = 12, max = 12, message = "Adhar number should be 12 character")
    private String adharNumber;

    @NotEmpty(message = "PAN number cannot blank")
    private String panCardNumber;

    @Email(message = "Email id not found")
    private String emailId;

    @NotEmpty(message = "Gender cannot blank")
    private String gender;

    @Override
    public String toString() {
        String messageBody = "customerName={0} | dateOfBirth={1} | " +
                "adharNumber={2} | PanNumber={3} | EmailId={4} | gender={5}";
        return java.text.MessageFormat.format(messageBody, customerName,
                dateOfBirth, adharNumber, panCardNumber, emailId, gender);
    }

}
