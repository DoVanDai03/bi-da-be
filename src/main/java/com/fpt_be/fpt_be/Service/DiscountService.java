package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;

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
} 