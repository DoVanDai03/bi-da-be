package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Dto.BrandDto;
import com.fpt_be.fpt_be.Entity.Brand;
import com.fpt_be.fpt_be.Repository.BrandRepository;

@Service
public class BrandService {
    
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand createBrand(BrandDto brandDto) {
        Brand brand = new Brand();
        brand.setTenThuongHieu(brandDto.getTenThuongHieu());
        brand.setMoTa(brandDto.getMoTa());
        brand.setTinhTrang(brandDto.getTinhTrang() != null ? brandDto.getTinhTrang() : 1);
        return brandRepository.save(brand);
    }

    public Brand updateBrand(Long id, BrandDto brandDto) {
        Optional<Brand> existingBrand = brandRepository.findById(id);
        if (existingBrand.isPresent()) {
            Brand brand = existingBrand.get();
            brand.setTenThuongHieu(brandDto.getTenThuongHieu());
            brand.setMoTa(brandDto.getMoTa());
            if (brandDto.getTinhTrang() != null) {
                brand.setTinhTrang(brandDto.getTinhTrang());
            }
            return brandRepository.save(brand);
        }
        throw new RuntimeException("Không tìm thấy thương hiệu với id: " + id);
    }

    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy thương hiệu với id: " + id);
        }
        brandRepository.deleteById(id);
    }

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu với id: " + id));
    }

    public Brand toggleBrandStatus(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu với id: " + id));
        
        // Toggle status between 0 and 1
        brand.setTinhTrang(brand.getTinhTrang() == 1 ? 0 : 1);
        
        return brandRepository.save(brand);
    }
} 