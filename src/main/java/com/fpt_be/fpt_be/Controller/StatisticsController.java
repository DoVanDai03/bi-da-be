package com.fpt_be.fpt_be.Controller;

import com.fpt_be.fpt_be.Dto.StatisticsDto;
import com.fpt_be.fpt_be.Service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/thong-ke")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<?> getStatistics(@RequestParam(defaultValue = "month") String period) {
        try {
            StatisticsDto statistics = statisticsService.getStatistics(period);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thống kê thành công",
                            "data", statistics
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", false,
                            "message", e.getMessage()
                    ));
        }
    }
} 