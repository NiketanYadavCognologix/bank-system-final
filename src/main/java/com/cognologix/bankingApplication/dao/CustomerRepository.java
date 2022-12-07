package com.cognologix.bankingApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognologix.bankingApplication.entities.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{
    Customer findByCustomerIdEquals(Integer customerId);

}
