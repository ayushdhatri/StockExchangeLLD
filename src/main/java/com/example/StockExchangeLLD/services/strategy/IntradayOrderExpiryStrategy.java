package com.example.StockExchangeLLD.services.strategy;

import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.models.OrderStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class IntradayOrderExpiryStrategy implements OrderExpiryStrategy{
    @Override
    public void checkExpiry(Order order) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime orderAcceptedTimeStamp = order.getOrderAcceptedTimeStamp();

        if(now.isAfter(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 15, 30)) &&
                orderAcceptedTimeStamp.isBefore(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 15, 30))
        ){
            order.setOrderStatus(OrderStatus.CANCELLED);
        }

    }
}
