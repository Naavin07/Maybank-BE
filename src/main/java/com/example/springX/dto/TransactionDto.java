package com.example.springX.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class TransactionDto {
    private Long id;

    private String accountNumber;

    private BigDecimal trxAmount;

    private String description;

    private LocalDate trxDate;

    private LocalTime trxTime;

    private String customerId;
}
