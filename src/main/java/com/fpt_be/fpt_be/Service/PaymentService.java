package com.fpt_be.fpt_be.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fpt_be.fpt_be.Dto.PaymentDto;
import com.fpt_be.fpt_be.Entity.Payment;
import com.fpt_be.fpt_be.Repository.PaymentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setIdKhachHang(paymentDto.getIdKhachHang());
        payment.setIdDonHang(paymentDto.getIdDonHang());
        payment.setNgayThanhToan(LocalDateTime.now());
        payment.setSoTien(paymentDto.getSoTien());
        payment.setPhuongThucThanhToan(paymentDto.getPhuongThucThanhToan());
        payment.setTrangThai("pending");

        // Set VNPay information if available
        payment.setVnp_BankCode(paymentDto.getVnp_BankCode());
        payment.setVnp_BankTranNo(paymentDto.getVnp_BankTranNo());
        payment.setVnp_CardType(paymentDto.getVnp_CardType());
        payment.setVnp_OrderInfo(paymentDto.getVnp_OrderInfo());
        payment.setVnp_TransactionNo(paymentDto.getVnp_TransactionNo());
        payment.setVnp_TransactionStatus(paymentDto.getVnp_TransactionStatus());
        payment.setVnp_SecureHash(paymentDto.getVnp_SecureHash());
        payment.setVnp_ResponseCode(paymentDto.getVnp_ResponseCode());
        payment.setVnp_PayDate(paymentDto.getVnp_PayDate());
        payment.setVnp_TmnCode(paymentDto.getVnp_TmnCode());
        payment.setVnp_TransactionType(paymentDto.getVnp_TransactionType());
        payment.setVnp_TxnRef(paymentDto.getVnp_TxnRef());
        payment.setVnp_Amount(paymentDto.getVnp_Amount());

        return paymentRepository.save(payment);
    }

    public Map<String, Object> processPayment(PaymentDto paymentDto) {
        try {
            // Kiểm tra thông tin thanh toán
            if (paymentDto.getIdDonHang() == null) {
                throw new RuntimeException("ID đơn hàng không được để trống");
            }
            if (paymentDto.getSoTien() == null || paymentDto.getSoTien() <= 0) {
                throw new RuntimeException("Số tiền thanh toán không hợp lệ");
            }
            if (paymentDto.getPhuongThucThanhToan() == null || paymentDto.getPhuongThucThanhToan().isEmpty()) {
                throw new RuntimeException("Phương thức thanh toán không được để trống");
            }

            // Tạo thanh toán mới
            Payment payment = createPayment(paymentDto);

            // Xử lý thanh toán theo phương thức
            String paymentResult = processPaymentMethod(paymentDto.getPhuongThucThanhToan(), paymentDto.getSoTien());

            // Cập nhật trạng thái thanh toán
            if ("success".equals(paymentResult)) {
                payment.setTrangThai("completed");
            } else {
                payment.setTrangThai("failed");
            }
            payment = paymentRepository.save(payment);

            return Map.of(
                "status", true,
                "message", "Thanh toán thành công",
                "data", payment
            );
        } catch (Exception e) {
            return Map.of(
                "status", false,
                "message", e.getMessage()
            );
        }
    }

    private String processPaymentMethod(String phuongThuc, Double soTien) {
        // Ví dụ: Gọi API của VNPAY, MOMO, etc.
        return "success"; // Tạm thời trả về thành công
    }

    public List<Payment> getCustomerPayments(Long idKhachHang) {
        return paymentRepository.findByIdKhachHang(idKhachHang);
    }

    // public Payment getCustomerPayment(Long idKhachHang, Long id) {
    //     return paymentRepository.findByIdAndIdKhachHang(id, idKhachHang)
    //             .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán"));
    // }

    public Payment updatePaymentStatus(Long id, String trangThai) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán với id: " + id));
        payment.setTrangThai(trangThai);
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByOrderId(Long idDonHang) {
        return paymentRepository.findByIdDonHang(idDonHang);
    }

    public List<Payment> getPaymentsByStatus(String trangThai) {
        return paymentRepository.findByTrangThai(trangThai);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán với id: " + id));
    }
} 