package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Dto.ProductDto;
import com.fpt_be.fpt_be.Entity.Product;
import com.fpt_be.fpt_be.Entity.Category;
import com.fpt_be.fpt_be.Entity.Brand;
import com.fpt_be.fpt_be.Entity.Discount;
import com.fpt_be.fpt_be.Entity.Supplier;
import com.fpt_be.fpt_be.Repository.ProductRepository;
import com.fpt_be.fpt_be.Repository.CategoryRepository;
import com.fpt_be.fpt_be.Repository.BrandRepository;
import com.fpt_be.fpt_be.Repository.DiscountRepository;
import com.fpt_be.fpt_be.Repository.SupplierRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private BrandRepository brandRepository;
    
    @Autowired
    private DiscountRepository discountRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAllWithJoins();
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setTenSanPham(productDto.getTenSanPham());
        product.setGiaSanPham(productDto.getGiaSanPham());
        product.setSoLuongTonKho(productDto.getSoLuongTonKho());
        product.setKichCo(productDto.getKichCo());
        product.setMauSac(productDto.getMauSac());
        product.setChatLieu(productDto.getChatLieu());
        product.setMoTa(productDto.getMoTa());
        product.setHinhAnh(productDto.getHinhAnh());
        product.setTrangThai(productDto.getTrangThai());

        // Set relationships
        if (productDto.getIdDanhMuc() != null) {
            Category category = categoryRepository.findById(productDto.getIdDanhMuc())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + productDto.getIdDanhMuc()));
            product.setDanhMuc(category);
        }

        if (productDto.getIdThuongHieu() != null) {
            Brand brand = brandRepository.findById(productDto.getIdThuongHieu())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu với id: " + productDto.getIdThuongHieu()));
            product.setThuongHieu(brand);
        }

        if (productDto.getIdGiamGia() != null) {
            Discount discount = discountRepository.findById(productDto.getIdGiamGia())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy mã giảm giá với id: " + productDto.getIdGiamGia()));
            product.setGiamGia(discount);
        }

        if (productDto.getIdNhaCungCap() != null) {
            Supplier supplier = supplierRepository.findById(productDto.getIdNhaCungCap())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với id: " + productDto.getIdNhaCungCap()));
            product.setNhaCungCap(supplier);
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto productDto) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setTenSanPham(productDto.getTenSanPham());
            product.setGiaSanPham(productDto.getGiaSanPham());
            product.setSoLuongTonKho(productDto.getSoLuongTonKho());
            product.setKichCo(productDto.getKichCo());
            product.setMauSac(productDto.getMauSac());
            product.setChatLieu(productDto.getChatLieu());
            product.setMoTa(productDto.getMoTa());
            product.setHinhAnh(productDto.getHinhAnh());
            product.setTrangThai(productDto.getTrangThai());

            // Update relationships
            if (productDto.getIdDanhMuc() != null) {
                Category category = categoryRepository.findById(productDto.getIdDanhMuc())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + productDto.getIdDanhMuc()));
                product.setDanhMuc(category);
            }

            if (productDto.getIdThuongHieu() != null) {
                Brand brand = brandRepository.findById(productDto.getIdThuongHieu())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu với id: " + productDto.getIdThuongHieu()));
                product.setThuongHieu(brand);
            }

            if (productDto.getIdGiamGia() != null) {
                Discount discount = discountRepository.findById(productDto.getIdGiamGia())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy mã giảm giá với id: " + productDto.getIdGiamGia()));
                product.setGiamGia(discount);
            }

            if (productDto.getIdNhaCungCap() != null) {
                Supplier supplier = supplierRepository.findById(productDto.getIdNhaCungCap())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với id: " + productDto.getIdNhaCungCap()));
                product.setNhaCungCap(supplier);
            }

            return productRepository.save(product);
        }
        throw new RuntimeException("Không tìm thấy sản phẩm với id: " + id);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sản phẩm với id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findByIdWithJoins(id);
    }
    
    /**
     * Lấy danh sách sản phẩm mới nhất theo số lượng giới hạn
     */
    public List<Product> getNewestProducts(Pageable pageable) {
        return productRepository.findNewestProducts(pageable);
    }
    
    /**
     * Lấy danh sách sản phẩm theo danh mục với giới hạn số lượng
     */
    public List<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findProductsByCategory(categoryId, pageable);
    }
    
    /**
     * Tìm kiếm sản phẩm theo từ khóa
     */
    public List<Product> searchProductsByKeyword(String keyword, Pageable pageable) {
        return productRepository.searchProductsByKeyword(keyword, pageable);
    }
    
    /**
     * Đếm số kết quả tìm kiếm theo từ khóa
     */
    public long countSearchResults(String keyword) {
        return productRepository.countSearchResults(keyword);
    }

    /**
     * Cập nhật số lượng tồn kho sau khi đặt hàng
     */
    public Product updateProductInventory(Long id, int soLuongDat) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));

        int soLuongTonKhoMoi = product.getSoLuongTonKho() - soLuongDat;
        
        if (soLuongTonKhoMoi < 0) {
            throw new RuntimeException("Số lượng đặt vượt quá số lượng tồn kho hiện có");
        }
        
        product.setSoLuongTonKho(soLuongTonKhoMoi);
        return productRepository.save(product);
    }
} 