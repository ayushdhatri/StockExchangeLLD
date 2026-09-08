package com.example.StockExchangeLLD.services;

import com.example.StockExchangeLLD.data.IOrderBook;
import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.OrderStatus;
import com.example.StockExchangeLLD.models.OrderType;
import com.example.StockExchangeLLD.models.Trade;
import com.example.StockExchangeLLD.services.strategy.OrderMatchingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradingService {
    private final IOrderBook orderBook;

    private final OrderMatchingStrategy orderMatchingStrategy;

    private final TradeService tradeService;// violating dependency principle

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public Order placeOrder(Order order){

        order.setOrderAcceptedTimeStamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setRemainingQuantity(order.getQuantity());

        orderBook.addOrder(order);// add the order in the order book

        executorService.submit(() -> {
           try{
               executeOrderMatch(order);
           }
           catch(Exception ex){
               log.error("Error executing order match");
           }
        });
        return order;

    }

    private void executeOrderMatch(Order newOrder){
        String stockSymbol = newOrder.getStockSymbol();
        List<Order> existingOrders = orderBook.getOrders(stockSymbol);

        existingOrders = existingOrders.stream().filter(order -> !order.getOrderId().equals(newOrder.getOrderId())).toList();

        List<Trade> executedTrades = orderMatchingStrategy.matchOrders(newOrder, existingOrders);

        if(!executedTrades.isEmpty()){
            for(Trade trade : executedTrades){
                // save trades in the db or in-memory
                tradeService.addTrade(trade);


            }
            orderBook.updateOrder(newOrder);

            for(Trade trade : executedTrades){
                String otherOrderId = newOrder.getOrderType() == OrderType.BUY ? trade.getSellerOrderId() : trade.getBuyerOrderId();
                orderBook.getOrderByOrderId(otherOrderId).ifPresent(orderBook::updateOrder);
            }

            log.info("order matches successfully");
        }

    }
}
