package com.opendevsociety.openstock.service;

import com.opendevsociety.openstock.dto.StockDto;
import com.opendevsociety.openstock.dto.WatchlistItemDto;
import com.opendevsociety.openstock.entity.WatchlistItem;
import com.opendevsociety.openstock.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchlistService {
    
    @Autowired
    private WatchlistRepository watchlistRepository;
    
    public List<WatchlistItemDto> getUserWatchlist(String userId) {
        List<WatchlistItem> items = watchlistRepository.findByUserId(userId);
        return items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public WatchlistItemDto addToWatchlist(String userId, String symbol, String company) {
        // Check if already exists
        if (watchlistRepository.existsByUserIdAndSymbol(userId, symbol)) {
            throw new IllegalArgumentException("Stock already in watchlist");
        }
        
        WatchlistItem item = new WatchlistItem(userId, symbol, company);
        WatchlistItem savedItem = watchlistRepository.save(item);
        return convertToDto(savedItem);
    }
    
    public void removeFromWatchlist(String userId, String symbol) {
        watchlistRepository.deleteByUserIdAndSymbol(userId, symbol);
    }
    
    public boolean isInWatchlist(String userId, String symbol) {
        return watchlistRepository.existsByUserIdAndSymbol(userId, symbol);
    }
    
    public List<String> getUserWatchlistSymbols(String userId) {
        List<WatchlistItem> items = watchlistRepository.findByUserId(userId);
        return items.stream()
                .map(WatchlistItem::getSymbol)
                .collect(Collectors.toList());
    }
    
    public void updateWatchlistStatus(List<StockDto> stocks, String userId) {
        List<String> watchlistSymbols = getUserWatchlistSymbols(userId);
        
        for (StockDto stock : stocks) {
            stock.setInWatchlist(watchlistSymbols.contains(stock.getSymbol()));
        }
    }
    
    private WatchlistItemDto convertToDto(WatchlistItem item) {
        WatchlistItemDto dto = new WatchlistItemDto();
        dto.setId(item.getId());
        dto.setUserId(item.getUserId());
        dto.setSymbol(item.getSymbol());
        dto.setCompany(item.getCompany());
        dto.setAddedAt(item.getAddedAt());
        return dto;
    }
}
