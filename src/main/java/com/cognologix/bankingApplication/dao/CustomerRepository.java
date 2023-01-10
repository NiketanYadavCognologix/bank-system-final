package com.cognologix.bankingApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognologix.bankingApplication.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{
    Customer findByCustomerIdEquals(Integer customerId);

    @Query(value = "SELECT * FROM customer WHERE adhar_number=?1 " +
            "OR pan_card_number=?2 OR email_id=?3", nativeQuery = true)
    Customer findByCustomerAdharNumberPanCardNumberEmailId(String adharNumber, String panCardNumber, String emailId);

    @Query(value = "SELECT * FROM customer WHERE customer_id !=?1 AND (adhar_number=?2 " +
            "OR pan_card_number=?3 OR email_id=?4)", nativeQuery = true)
    Customer findSimilarToAdharNumberPanCardNumberEmailId(Integer customerId, String adharNumber, String panCardNumber, String emailId);

}
