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
@RequestMapping("/api/user/payment")
public class HomePaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentDto paymentDto) {
        try {
            Map<String, Object> result = paymentService.processPayment(paymentDto);
            if ((Boolean) result.get("status")) {
                return ResponseEntity.ok()
                        .body(Map.of(
                                "status", true,
                                "message", "Tạo thanh toán thành công",
                                "data", result.get("data")));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", false,
                                "message", result.get("message")));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/khach-hang/{idKhachHang}")
    public ResponseEntity<?> getCustomerPayments(@PathVariable Long idKhachHang) {
        try {
            List<Payment> payments = paymentService.getCustomerPayments(idKhachHang);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách thanh toán thành công",
                            "data", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    // @GetMapping("/khach-hang/{idKhachHang}/{id}")
    // public ResponseEntity<?> getCustomerPayment(
    //         @PathVariable Long idKhachHang,
    //         @PathVariable Long id) {
    //     try {
    //         Payment payment = paymentService.getCustomerPayment(idKhachHang, id);
    //         return ResponseEntity.ok()
    //                 .body(Map.of(
    //                         "status", true,
    //                         "message", "Lấy thông tin thanh toán thành công",
    //                         "data", payment));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest()
    //                 .body(Map.of("status", false, "message", e.getMessage()));
    //     }
    // }
}
