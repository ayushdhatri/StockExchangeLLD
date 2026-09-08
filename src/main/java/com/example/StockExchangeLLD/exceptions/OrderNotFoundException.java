package com.example.StockExchangeLLD.exceptions;

import org.springframework.stereotype.Component;


public class OrderNotFoundException extends TradingException{

    public OrderNotFoundException(String orderId){
        super("Order id not found :" + orderId);
    }

}
