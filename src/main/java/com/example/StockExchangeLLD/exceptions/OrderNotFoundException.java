package com.example.StockExchangeLLD.exceptions;

public class OrderNotFoundException extends TradingException{

    public OrderNotFoundException(String orderId){
        super("Order id not found :" + orderId);
    }

}
