package com.example.StockExchangeLLD.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Builder.Default
    private String userId = UUID.randomUUID().toString();

    @NotBlank(message = "username is required")
    @Column(nullable = false)
    private String userName;

    @Email(message = "Invalid Email Address")
    private String email;

    private String phoneNumber;



}
