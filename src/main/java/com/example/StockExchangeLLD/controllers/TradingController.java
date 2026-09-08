package com.example.StockExchangeLLD.controllers;

import com.example.StockExchangeLLD.dtos.OrderRequest;
import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.services.TradingService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trading")
@RequiredArgsConstructor
public class TradingController {
    private final TradingService tradingService; // Violated DIP, TODO: FIX THIS

    @PostMapping("/orders")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest){
        Order order = tradingService.placeOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

}
