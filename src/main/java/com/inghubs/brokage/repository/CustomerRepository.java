package com.inghubs.brokage.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inghubs.brokage.entity.BrokageCustomer;

@Repository
public interface CustomerRepository extends JpaRepository<BrokageCustomer, Long> {
        Optional<BrokageCustomer> findByUsername(String username);
}
