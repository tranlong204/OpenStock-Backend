package com.opendevsociety.openstock.repository;

import com.opendevsociety.openstock.entity.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {
    
    List<Alert> findByUserId(String userId);
    
    List<Alert> findByUserIdAndActiveTrue(String userId);
    
    List<Alert> findBySymbolAndActiveTrue(String symbol);
    
    List<Alert> findByActiveTrue();
}
