package com.example.StockExchangeLLD;

import com.example.StockExchangeLLD.models.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String userId;

    private OrderType orderType;

    private String stockId;

    private int quantity;

    private double price;

}

