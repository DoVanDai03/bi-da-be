package com.fpt_be.fpt_be.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fpt_be.fpt_be.Dto.PaymentDto;
import com.fpt_be.fpt_be.Entity.Payment;
import com.fpt_be.fpt_be.Service.PaymentService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentDto paymentDto) {
        try {
            Payment payment = paymentService.createPayment(paymentDto);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Tạo thanh toán thành công",
                            "data", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String trangThai = request.get("trangThai");
            if (trangThai == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Trạng thái không được để trống"));
            }

            Payment payment = paymentService.updatePaymentStatus(id, trangThai);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật trạng thái thanh toán thành công",
                            "data", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/order/{idDonHang}")
    public ResponseEntity<?> getPaymentsByOrderId(@PathVariable Long idDonHang) {
        try {
            List<Payment> payments = paymentService.getPaymentsByOrderId(idDonHang);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách thanh toán theo đơn hàng thành công",
                            "data", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/status/{trangThai}")
    public ResponseEntity<?> getPaymentsByStatus(@PathVariable String trangThai) {
        try {
            List<Payment> payments = paymentService.getPaymentsByStatus(trangThai);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách thanh toán theo trạng thái thành công",
                            "data", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin thanh toán thành công",
                            "data", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 