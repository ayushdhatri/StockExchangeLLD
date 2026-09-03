package com.example.StockExchangeLLD.data;

import com.example.StockExchangeLLD.models.Order;

import java.util.List;

public interface IOrderBook {
    void addOrder(Order order);

    void removeOrder(String orderId, String stockSymbol);

    List<Order> getOrders(String stockSymbol);

    boolean updateOrder(Order updatedOrder);
}

