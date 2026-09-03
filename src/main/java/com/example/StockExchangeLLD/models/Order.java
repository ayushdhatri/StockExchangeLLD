package com.example.StockExchangeLLD.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Builder.Default
    private String orderId = UUID.randomUUID().toString();

    @NotBlank
    private String userId;

    @NotNull(message = "orderType cannot be null")
    private OrderType orderType;

    @NotNull(message = "orderStatus cannot be null")
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.ACCEPTED;

    @NotNull(message = "Order cannot exist with stock")
    private String stockId;

    @NotNull(message = "Quantity is required")
    private int quantity;

    @NotNull(message = "Price is required")
    private double price;

    @Builder.Default
    private int filledQuantity = 0;

    @Builder.Default
    private int remainingQuantity = 0;

    @Builder.Default
    private LocalDateTime orderAcceptedTimeStamp = LocalDateTime.now();



}
