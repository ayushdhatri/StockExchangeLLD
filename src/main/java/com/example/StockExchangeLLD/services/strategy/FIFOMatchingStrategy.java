package com.example.StockExchangeLLD.services.strategy;

import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.OrderStatus;
import com.example.StockExchangeLLD.models.OrderType;
import com.example.StockExchangeLLD.models.Trade;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j

public class FIFOMatchingStrategy implements OrderMatchingStrategy{
    @Override
    public String getStrategyName() {
        return "FIFOMatchingStrategy";
    }

    @Override
    public List<Trade> matchOrders(Order newOrder, List<Order> existingOrders) {
        // first we need to decide if newOrder is of type
        // -> Buy
        //    1) filter all the order from existingOrders which are of type sell
        //    2) Out of all the sell order, the orders will be listed in fifoManner according to timestamp
        //    3) check for each of the order, if buyPrice >= sellPrice, then in that we can buy the quantity required
        //    4) It might be the case that one sell order is not able to full my buy order then we need to more than one trade
        //    5) every time we buy from a seller we create a new trade updates it details
        // -> Sell
        //    1) Filter all the order from existingOrder which are of type Buy

        if(newOrder.getOrderType() == OrderType.BUY){
            return matchBuyOrder(newOrder, existingOrders);
        }
        else if(newOrder.getOrderType() == OrderType.SELL){
            return matchSellOrder(newOrder, existingOrders);

        }
        return List.of();

    }

    private List<Trade> matchBuyOrder(Order newOrder, List<Order> existingOrder){
        List<Trade> trades = new ArrayList<>();
        List<Order> matchingSellOrders = existingOrder.stream()
                .filter(order -> order.getOrderType() == OrderType.SELL)
                .filter(order -> order.getStockId().equals(newOrder.getStockId()))
                .filter(order -> order.getPrice() <= newOrder.getPrice())
                .filter(order -> order.getOrderStatus().equals(OrderStatus.ACCEPTED))
                .sorted(Comparator.comparing(Order::getPrice).thenComparing(Order::getOrderAcceptedTimeStamp))
                .toList();

        int remainingQuantity = newOrder.getRemainingQuantity();
        for(Order sellOrder : matchingSellOrders){
            if(remainingQuantity <=0)break;
            int tradeQuantity = Math.min(remainingQuantity, sellOrder.getRemainingQuantity());
            double tradePrice = sellOrder.getPrice();

            Trade trade = Trade.builder()
                    .buyerOrderId(newOrder.getOrderId())
                    .sellerOrderId(sellOrder.getOrderId())
                    .stockId(newOrder.getStockId())
                    .quantity(tradeQuantity)
                    .price(tradePrice)
                    .build();
            trades.add(trade);

            newOrder.setFilledQuantity(newOrder.getFilledQuantity() + tradeQuantity);
            newOrder.setRemainingQuantity(newOrder.getRemainingQuantity() - tradeQuantity);

            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);
            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQuantity);

            remainingQuantity-=tradeQuantity;
            log.info("Trade : {} - {} - {} - {}",trade.getTradeId(), trade.getBuyerOrderId(), trade.getSellerOrderId(), trade.getQuantity());
        }


        return trades;
    }

    private List<Trade> matchSellOrder(Order sellOrder, List<Order> existingOrder){
        // I am looking for the buyers
        List<Trade> trades = new ArrayList<>();

        List<Order> matchingBuyerOrder = existingOrder.stream()
                .filter((order) -> order.getOrderType().equals(OrderType.BUY))
                .filter(order -> order.getStockId().equals(sellOrder.getStockId()))
                .filter(order -> order.getPrice() >= sellOrder.getPrice())
                .filter(order -> order.getOrderStatus().equals(OrderStatus.ACCEPTED))
                .sorted(Comparator.comparing(Order::getPrice, Comparator.reverseOrder()).thenComparing(Order::getOrderAcceptedTimeStamp))
                .toList();

        int remainingQuantity = sellOrder.getRemainingQuantity();
        for(Order buyOrder : matchingBuyerOrder){
            if(remainingQuantity <= 0)break;
            int tradeQuantity = Math.min(remainingQuantity, buyOrder.getRemainingQuantity());
            double tradePrice = buyOrder.getPrice();

            Trade trade = Trade.builder()
                    .buyerOrderId(buyOrder.getOrderId())
                    .sellerOrderId(sellOrder.getOrderId())
                    .stockId(sellOrder.getStockId())
                    .quantity(tradeQuantity)
                    .price(tradePrice)
                    .build();
            trades.add(trade);

            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQuantity);
            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);

            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQuantity);
            buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity() - tradeQuantity);

            remainingQuantity-=tradeQuantity;
            log.info("Trade : {} - {} - {} - {}",trade.getTradeId(), trade.getBuyerOrderId(), trade.getSellerOrderId(), trade.getQuantity());



        }



        return trades;
    }
}
