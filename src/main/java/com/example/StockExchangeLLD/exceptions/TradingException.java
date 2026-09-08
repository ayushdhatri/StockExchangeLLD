package com.example.StockExchangeLLD.exceptions;

import com.example.StockExchangeLLD.models.Trade;
import org.springframework.stereotype.Component;


public class TradingException extends Exception{

    public TradingException(String message){
        super(message);
    }

    public TradingException(String message, Throwable cause){
        super(message, cause);
    }
}
