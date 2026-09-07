package com.example.StockExchangeLLD.data;

import com.example.StockExchangeLLD.models.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderBook {
    void addOrder(Order order);

    boolean removeOrder(String orderId, String stockSymbol);

    List<Order> getOrders(String stockSymbol);

    boolean updateOrder(Order updatedOrder);

}

