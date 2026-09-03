package com.example.StockExchangeLLD.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    @Builder.Default
    private String tradeId = UUID.randomUUID().toString();

    @NotBlank(message = "Buyer order id is required")
    private String buyerOrderId;

    @NotBlank(message = "Seller order id is required")
    private String sellerOrderId;

    @NotBlank(message = "Stock Id is required")
    private String stockId;

    @NotBlank(message = "Quantity is required")
    private int quantity;

    @NotBlank(message = "price is required")
    private double price;






}
