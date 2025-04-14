package com.fpt_be.fpt_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
    private Map<String, Integer> orderStatus;
    private List<RevenueData> revenue;
    private int totalOrders;
    private double totalRevenue;
    private int completedOrders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueData {
        private String date;
        private double amount;
    }
} 