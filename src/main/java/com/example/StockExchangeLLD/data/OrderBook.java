package com.example.StockExchangeLLD.data;

import com.example.StockExchangeLLD.models.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Slf4j

public class OrderBook implements IOrderBook {

    private final ConcurrentMap<String, List<Order>> orderBook = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, ReadWriteLock> symbolLocks = new ConcurrentHashMap<>();

    @Override
    public void addOrder(Order order) {
        // we  need to figure out the stock symbol
        String stockSymbol = order.getStockSymbol();

        // get the lock for particular stockSymbol
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.writeLock().lock();
        try{
            orderBook.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(order);// adding the order
            log.info("Order added to order book: {} - {} - {} - {} - {}- {}", order.getOrderId(), order.getOrderStatus(),order.getOrderType(), order.getStockSymbol(), order.getPrice(), order.getQuantity());
        }
        finally{
            lock.writeLock().unlock();// always unlock the symbol lock
        }
    }

    private ReadWriteLock getOrCreateLock(String stockSymbol){
        return symbolLocks.computeIfAbsent(stockSymbol, k -> new ReentrantReadWriteLock());
    }


    @Override
    public boolean removeOrder(String orderId, String stockSymbol) {
        // Fetch the the lock first
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.writeLock().lock();
        try{
            List<Order> orders = orderBook.get(stockSymbol);
            if(orders != null){
                boolean removed = orders.removeIf(order -> order.getOrderId().equals(orderId));
                if(removed){
                    log.info("Order removed from order book");
                }
                else{
                    log.info("Order not found in order book");
                }
                return removed;
            }
            return false;
        }
        finally{
            lock.writeLock().unlock();
        }


    }

    @Override
    public List<Order> getOrders(String stockSymbol) {
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.readLock().lock();
        try{
            List<Order> orders  = orderBook.get(stockSymbol);
            if(orders != null){
                log.info("Order fetched successfully for stock symbol : {}", stockSymbol);
                return List.copyOf(orders);
            }
            else {
                log.info("Order not found with stock symbol : {}", stockSymbol);
                return List.of();
            }
        }
        finally{
            lock.readLock().unlock();

        }
    }

    @Override
    public boolean updateOrder(Order updatedOrder) {
        // here we can first remove this order
        // and then add this updateOrder
        ReadWriteLock lock = getOrCreateLock(updatedOrder.getStockSymbol());
        lock.writeLock().lock();
        try{
            List<Order> orders = orderBook.get(updatedOrder.getStockSymbol());
            boolean hasOrder = orders.removeIf(order -> order.getOrderId().equals(updatedOrder.getOrderId()));
            if(hasOrder){
                orders.add(updatedOrder);
                log.info("Existing order has been updateed successfully");
                return true;
            }
            else {
                // order does not exist
                // we cannot update the order
                log.info("Order does not exist with id: {} ", updatedOrder.getOrderId());
                return false;
            }
        }
        finally{
            lock.writeLock().unlock();
        }
    }
}
