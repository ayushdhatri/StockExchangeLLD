package com.example.StockExchangeLLD.data;

import com.example.StockExchangeLLD.models.Order;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;

public class OrderBook implements IOrderBook {

    private final ConcurrentMap<String, List<Order>> orderBook = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, ReadWriteLock> symbolLocks = new ConcurrentHashMap<>();

    @Override
    public void addOrder(Order order) {

    }

    @Override
    public void removeOrder(String orderId, String stockSymbol) {

    }

    @Override
    public List<Order> getOrders(String stockSymbol) {
        return List.of();
    }

    @Override
    public boolean updateOrder(Order updatedOrder) {
        return false;
    }
}
