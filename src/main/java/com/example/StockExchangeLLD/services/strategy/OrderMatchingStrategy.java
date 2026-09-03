package com.example.StockExchangeLLD.services.strategy;

import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.Trade;

import java.util.List;

public interface OrderMatchingStrategy {

    String getStrategyName();

    List<Trade> matchOrders(Order newOrder, List<Order> existingOrders);

}
