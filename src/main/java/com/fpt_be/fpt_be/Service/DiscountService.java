package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Dto.DiscountDto;
import com.fpt_be.fpt_be.Entity.Discount;
import com.fpt_be.fpt_be.Repository.DiscountRepository;

@Service
public class DiscountService {
    
    @Autowired
    private DiscountRepository discountRepository;

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount createDiscount(DiscountDto discountDto) {
        Discount discount = new Discount();
        discount.setMaCode(discountDto.getMaCode());
        discount.setPhamTramGiamGia(discountDto.getPhamTramGiamGia());
        discount.setThanhTien(discountDto.getThanhTien());
        discount.setNgayBatDau(discountDto.getNgayBatDau());
        discount.setNgayKetThuc(discountDto.getNgayKetThuc());
        return discountRepository.save(discount);
    }

    public Discount updateDiscount(Long id, DiscountDto discountDto) {
        Optional<Discount> existingDiscount = discountRepository.findById(id);
        if (existingDiscount.isPresent()) {
            Discount discount = existingDiscount.get();
            discount.setMaCode(discountDto.getMaCode());
            discount.setPhamTramGiamGia(discountDto.getPhamTramGiamGia());
            discount.setThanhTien(discountDto.getThanhTien());
            discount.setNgayBatDau(discountDto.getNgayBatDau());
            discount.setNgayKetThuc(discountDto.getNgayKetThuc());
            return discountRepository.save(discount);
        }
        throw new RuntimeException("Không tìm thấy mã giảm giá với id: " + id);
    }

    public void deleteDiscount(Long id) {
        if (!discountRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy mã giảm giá với id: " + id);
        }
        discountRepository.deleteById(id);
    }

    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã giảm giá với id: " + id));
    }

    /**
     * Tìm mã giảm giá theo mã code
     */
    public Optional<Discount> findByMaCode(String maCode) {
        return discountRepository.findByMaCode(maCode);
    }
    
    /**
     * Tìm và kiểm tra mã giảm giá hợp lệ
     */
    public Optional<Discount> findValidDiscount(String maCode) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByMaCodeAndNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(
                maCode, now, now);
    }
    
    /**
     * Kiểm tra mã giảm giá có hợp lệ không
     */
    public boolean isDiscountValid(String maCode) {
        return findValidDiscount(maCode).isPresent();
    }
    
    /**
     * Kiểm tra mã giảm giá có áp dụng cho sản phẩm không
     */
    public boolean isDiscountApplicableToProduct(Long discountId, Long productId) {
        // Kiểm tra mã giảm giá có tồn tại không
        Optional<Discount> discount = discountRepository.findById(discountId);
        if (discount.isEmpty()) {
            return false;
        }
        
        // Kiểm tra mã giảm giá có trong thời hạn không
        LocalDateTime now = LocalDateTime.now();
        if (discount.get().getNgayBatDau().isAfter(now) || discount.get().getNgayKetThuc().isBefore(now)) {
            return false;
        }
        
        // Ở đây ta giả định mã giảm giá áp dụng cho tất cả sản phẩm
        // Nếu cần kiểm tra mã giảm giá chỉ áp dụng cho sản phẩm cụ thể,
        // sẽ cần thêm bảng quan hệ giữa discount và product
        
        return true;
    }
} 