package com.inghubs.brokage.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inghubs.brokage.entity.BrokageOrder;

public interface OrderRepository extends JpaRepository<BrokageOrder, Long> {

    List<BrokageOrder> findAllByCreateDateBetween(LocalDate starDate, LocalDate endDate);
    List<BrokageOrder> findByCreateDateBetweenAndCustomerId(LocalDate starDate, LocalDate endDate, long customerId);

}
