package com.example.StockExchangeLLD.exceptions;

import org.springframework.stereotype.Component;


public class UserNotFoundException extends TradingException{

    public UserNotFoundException(String userId){
        super("User not found with id : " + userId);
    }
}
