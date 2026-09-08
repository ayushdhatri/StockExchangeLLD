package com.example.StockExchangeLLD.services;

import com.example.StockExchangeLLD.models.Trade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
@RequiredArgsConstructor

public class TradeService {
    private final Map<String, Trade> trades = new ConcurrentHashMap<>();

    Trade addTrade(Trade trade){
        String tradeId = trade.getTradeId();
        this.trades.put(tradeId, trade);
        return trade;
    }

    Optional<Trade> getTrade(String tradeId){
        if(trades.containsKey(tradeId)){
            return Optional.of(trades.get(tradeId));
        }
        return Optional.empty();
    }

    void removeTrade(String tradeId){
        Optional<Trade> tradeWithTradeId = getTrade(tradeId);
        if(tradeWithTradeId.isPresent()){
            trades.remove(tradeId);
            log.info("Trade with id {} removed successfully", tradeId);
            return;
        }
        log.info("Trade with id {} does not exist", tradeId);
    }


}
