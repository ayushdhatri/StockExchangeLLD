package com.example.StockExchangeLLD.controllers;

import com.example.StockExchangeLLD.data.IOrderBook;
import com.example.StockExchangeLLD.dtos.OrderRequest;
import com.example.StockExchangeLLD.models.Order;
import com.example.StockExchangeLLD.services.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/orderBook/{symbol}")
    public ResponseEntity<List<Order>> getOrderBook(@PathVariable("symbol") String symbol){
        List<Order> symbolOrders = tradingService.getOrderBySymbol(symbol);
        return ResponseEntity.status(HttpStatus.OK).body(symbolOrders);
    }

}
