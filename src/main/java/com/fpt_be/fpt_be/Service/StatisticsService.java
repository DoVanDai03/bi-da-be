package com.fpt_be.fpt_be.Service;

import com.fpt_be.fpt_be.Dto.StatisticsDto;
import com.fpt_be.fpt_be.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    public StatisticsDto getStatistics(String period) {
        LocalDateTime startDate = getStartDate(period);
        LocalDateTime endDate = LocalDateTime.now();

        StatisticsDto statistics = new StatisticsDto();
        
        // Get order status statistics
        Map<String, Integer> orderStatus = new HashMap<>();
        orderStatus.put("pending", orderRepository.countByStatusAndCreatedAtBetween("pending", startDate, endDate));
        orderStatus.put("processing", orderRepository.countByStatusAndCreatedAtBetween("processing", startDate, endDate));
        orderStatus.put("shipped", orderRepository.countByStatusAndCreatedAtBetween("shipped", startDate, endDate));
        orderStatus.put("completed", orderRepository.countByStatusAndCreatedAtBetween("completed", startDate, endDate));
        orderStatus.put("cancelled", orderRepository.countByStatusAndCreatedAtBetween("cancelled", startDate, endDate));
        orderStatus.put("returned", orderRepository.countByStatusAndCreatedAtBetween("returned", startDate, endDate));
        statistics.setOrderStatus(orderStatus);

        // Get revenue data
        List<StatisticsDto.RevenueData> revenueData = new ArrayList<>();
        LocalDate currentDate = startDate.toLocalDate();
        while (!currentDate.isAfter(endDate.toLocalDate())) {
            double dailyRevenue = orderRepository.sumRevenueByDate(currentDate);
            revenueData.add(new StatisticsDto.RevenueData(currentDate.toString(), dailyRevenue));
            currentDate = currentDate.plusDays(1);
        }
        statistics.setRevenue(revenueData);

        // Get total statistics
        statistics.setTotalOrders(orderRepository.countByCreatedAtBetween(startDate, endDate));
        statistics.setTotalRevenue(orderRepository.sumRevenueByDateRange(startDate, endDate));
        statistics.setCompletedOrders(orderRepository.countByStatusAndCreatedAtBetween("completed", startDate, endDate));

        return statistics;
    }

    private LocalDateTime getStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period) {
            case "day" -> now.truncatedTo(ChronoUnit.DAYS);
            case "week" -> now.minusWeeks(1);
            case "month" -> now.minusMonths(1);
            case "year" -> now.minusYears(1);
            default -> now.minusMonths(1);
        };
    }
} 