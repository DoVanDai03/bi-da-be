package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Dto.SupplierDto;
import com.fpt_be.fpt_be.Entity.Supplier;
import com.fpt_be.fpt_be.Repository.SupplierRepository;

@Service
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(SupplierDto supplierDto) {
        Supplier supplier = new Supplier();
        supplier.setTenNhaCungCap(supplierDto.getTenNhaCungCap());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setSoDienThoai(supplierDto.getSoDienThoai());
        supplier.setDiaChi(supplierDto.getDiaChi());
        supplier.setNguoiDaiDien(supplierDto.getNguoiDaiDien());
        supplier.setTrangThai(supplierDto.getTrangThai());
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, SupplierDto supplierDto) {
        Optional<Supplier> existingSupplier = supplierRepository.findById(id);
        if (existingSupplier.isPresent()) {
            Supplier supplier = existingSupplier.get();
            supplier.setTenNhaCungCap(supplierDto.getTenNhaCungCap());
            supplier.setEmail(supplierDto.getEmail());
            supplier.setSoDienThoai(supplierDto.getSoDienThoai());
            supplier.setDiaChi(supplierDto.getDiaChi());
            supplier.setNguoiDaiDien(supplierDto.getNguoiDaiDien());
            supplier.setTrangThai(supplierDto.getTrangThai());
            return supplierRepository.save(supplier);
        }
        throw new RuntimeException("Không tìm thấy nhà cung cấp với id: " + id);
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy nhà cung cấp với id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với id: " + id));
    }
} 