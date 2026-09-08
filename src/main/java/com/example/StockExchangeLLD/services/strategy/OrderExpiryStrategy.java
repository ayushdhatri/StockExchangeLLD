package com.example.StockExchangeLLD.services.strategy;

import com.example.StockExchangeLLD.models.Order;

public interface OrderExpiryStrategy {

    void checkExpiry(Order order);

}
