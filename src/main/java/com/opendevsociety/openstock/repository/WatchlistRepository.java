package com.opendevsociety.openstock.repository;

import com.opendevsociety.openstock.entity.WatchlistItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends MongoRepository<WatchlistItem, String> {
    
    List<WatchlistItem> findByUserId(String userId);
    
    Optional<WatchlistItem> findByUserIdAndSymbol(String userId, String symbol);
    
    boolean existsByUserIdAndSymbol(String userId, String symbol);
    
    void deleteByUserIdAndSymbol(String userId, String symbol);
    
    List<String> findSymbolsByUserId(String userId);
}
