package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Dto.CartDto;
import com.fpt_be.fpt_be.Dto.CartWithProductDto;
import com.fpt_be.fpt_be.Entity.Cart;
import com.fpt_be.fpt_be.Entity.Product;
import com.fpt_be.fpt_be.Entity.Discount;
import com.fpt_be.fpt_be.Repository.CartRepository;
import com.fpt_be.fpt_be.Repository.ProductRepository;


@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private DiscountService discountService;



    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
    
    public List<Cart> getCartsByUserId(Long idKhachHang) {
        return cartRepository.findByIdKhachHang(idKhachHang);
    }
    
    public List<Cart> getCartsByOrderId(Long idDonHang) {
        return cartRepository.findByIdDonHang(idDonHang);
    }

    public Cart createCart(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setIdKhachHang(cartDto.getIdKhachHang());
        cart.setIdSanPham(cartDto.getIdSanPham());
        cart.setIdDonHang(cartDto.getIdDonHang());
        cart.setSoLuong(cartDto.getSoLuong());
        cart.setThanhTien(cartDto.getThanhTien());
        cart.setTrangThai(cartDto.getTrangThai());
        return cartRepository.save(cart);
    }

    public Cart updateCart(Long id, CartDto cartDto) {
        Optional<Cart> existingCart = cartRepository.findById(id);
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setIdKhachHang(cartDto.getIdKhachHang());
            cart.setIdSanPham(cartDto.getIdSanPham());
            cart.setIdDonHang(cartDto.getIdDonHang());
            cart.setSoLuong(cartDto.getSoLuong());
            cart.setThanhTien(cartDto.getThanhTien());
            cart.setTrangThai(cartDto.getTrangThai());
            return cartRepository.save(cart);
        }
        throw new RuntimeException("Không tìm thấy giỏ hàng với id: " + id);
    }

    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy giỏ hàng với id: " + id);
        }
        cartRepository.deleteById(id);
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng với id: " + id));
    }
    
    // Phương thức thêm sản phẩm vào giỏ hàng
    public Cart addProductToCart(Long idKhachHang, Long idSanPham, Integer soLuong, Double thanhTien) {
        // Kiểm tra số lượng tồn kho
        Product product = productRepository.findByIdWithJoins(idSanPham);
        if (product == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm với id: " + idSanPham);
        }
        
        if (product.getSoLuongTonKho() == null) {
            throw new RuntimeException("Số lượng tồn kho của sản phẩm chưa được cập nhật");
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<Cart> existingCartItem = cartRepository.findByIdKhachHangAndIdSanPhamAndTrangThai(idKhachHang, idSanPham, "active");
        
        if (existingCartItem.isPresent()) {
            // Nếu sản phẩm đã có trong giỏ hàng với trạng thái "active", cập nhật số lượng và thành tiền
            Cart cartItem = existingCartItem.get();
            int newQuantity = cartItem.getSoLuong() + soLuong;
            
            // Kiểm tra số lượng mới có vượt quá tồn kho không
            if (newQuantity > product.getSoLuongTonKho()) {
                throw new RuntimeException("Số lượng không thể vượt quá " + product.getSoLuongTonKho() + " sản phẩm trong kho!");
            }
            
            cartItem.setSoLuong(newQuantity);
            cartItem.setThanhTien(cartItem.getThanhTien() + thanhTien);
            return cartRepository.save(cartItem);
        } else {
            // Kiểm tra số lượng yêu cầu có vượt quá tồn kho không
            if (soLuong > product.getSoLuongTonKho()) {
                throw new RuntimeException("Số lượng không thể vượt quá " + product.getSoLuongTonKho() + " sản phẩm trong kho!");
            }
            
            // Nếu sản phẩm chưa có trong giỏ hàng hoặc có trạng thái "ordered", tạo mới
            Cart newCartItem = new Cart();
            newCartItem.setIdKhachHang(idKhachHang);
            newCartItem.setIdSanPham(idSanPham);
            newCartItem.setSoLuong(soLuong);
            newCartItem.setThanhTien(thanhTien);
            newCartItem.setTrangThai("active");
            return cartRepository.save(newCartItem);
        }
    }
    
    // Phương thức cập nhật số lượng sản phẩm trong giỏ hàng
    public Cart updateProductQuantity(Long idKhachHang, Long idSanPham, Integer soLuong, Double thanhTien) {
        // Kiểm tra số lượng tồn kho
        Product product = productRepository.findByIdWithJoins(idSanPham);
        if (product == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm với id: " + idSanPham);
        }
        
        if (product.getSoLuongTonKho() == null) {
            throw new RuntimeException("Số lượng tồn kho của sản phẩm chưa được cập nhật");
        }

        // Kiểm tra số lượng yêu cầu có vượt quá tồn kho không
        if (soLuong > product.getSoLuongTonKho()) {
            throw new RuntimeException("Số lượng không thể vượt quá " + product.getSoLuongTonKho() + " sản phẩm trong kho!");
        }

        // Tìm sản phẩm trong giỏ hàng
        Optional<Cart> cartItem = cartRepository.findByIdKhachHangAndIdSanPhamAndTrangThai(idKhachHang, idSanPham, "active");
        
        if (cartItem.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        
        Cart cart = cartItem.get();
        cart.setSoLuong(soLuong);
        cart.setThanhTien(thanhTien);
        
        return cartRepository.save(cart);
    }
    
    // Phương thức xóa sản phẩm khỏi giỏ hàng
    public void removeProductFromCart(Long idKhachHang, Long idSanPham) {
        // Tìm sản phẩm trong giỏ hàng
        Optional<Cart> cartItem = cartRepository.findByIdKhachHangAndIdSanPhamAndTrangThai(idKhachHang, idSanPham, "active");
        
        if (cartItem.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        
        cartRepository.delete(cartItem.get());
    }
    
    // Phương thức gán đơn hàng ID cho giỏ hàng
    public void assignOrderToCart(Long idKhachHang, Long idDonHang) {
        List<Cart> cartItems = cartRepository.findByIdKhachHang(idKhachHang);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng của khách hàng");
        }
        
        for (Cart cartItem : cartItems) {
            if ("active".equals(cartItem.getTrangThai())) {
                cartItem.setIdDonHang(idDonHang);
                cartItem.setTrangThai("ordered");
                cartRepository.save(cartItem);
            }
        }
    }

    /**
     * Lấy giỏ hàng của khách hàng kèm thông tin sản phẩm
     */
    public List<CartWithProductDto> getCartsWithProductInfo(Long idKhachHang) {
        List<Cart> carts = cartRepository.findByIdKhachHangAndTrangThai(idKhachHang, "active");
        return carts.stream()
                .map(cart -> {
                    CartWithProductDto dto = new CartWithProductDto();
                    dto.setId(cart.getId());
                    dto.setIdKhachHang(cart.getIdKhachHang());
                    dto.setIdSanPham(cart.getIdSanPham());
                    dto.setSoLuong(cart.getSoLuong());
                    dto.setThanhTien(cart.getThanhTien());
                    dto.setTrangThai(cart.getTrangThai());
                    dto.setIdDonHang(cart.getIdDonHang());
                    
                    // Lấy thông tin sản phẩm
                    Product product = productRepository.findByIdWithJoins(cart.getIdSanPham());
                    if (product != null) {
                        dto.setTenSanPham(product.getTenSanPham());
                        dto.setGiaSanPham(product.getGiaSanPham());
                        dto.setHinhAnh(product.getHinhAnh());
                        dto.setSoLuongTonKho(product.getSoLuongTonKho());
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Áp dụng mã giảm giá cho giỏ hàng
     * 
     * @param idKhachHang id của khách hàng
     * @param maGiamGia mã giảm giá người dùng nhập vào
     * @param idSanPham id của sản phẩm cần áp dụng mã giảm giá (null nếu áp dụng cho toàn bộ giỏ hàng)
     * @return Thông tin kết quả áp dụng mã giảm giá
     */
    public Map<String, Object> applyDiscountCode(Long idKhachHang, String maGiamGia, Long idSanPham) {
        Map<String, Object> result = new HashMap<>();
        
        // Kiểm tra mã giảm giá có tồn tại và hợp lệ không
        Optional<Discount> discountOpt = discountService.findValidDiscount(maGiamGia);
        if (discountOpt.isEmpty()) {
            result.put("status", false);
            result.put("message", "Mã giảm giá không hợp lệ hoặc đã hết hạn");
            return result;
        }
        
        Discount discount = discountOpt.get();
        
        // Lấy danh sách giỏ hàng của khách hàng
        List<Cart> carts;
        if (idSanPham != null) {
            // Nếu có idSanPham, chỉ áp dụng cho sản phẩm cụ thể
            Optional<Cart> cartItem = cartRepository.findByIdKhachHangAndIdSanPhamAndTrangThai(idKhachHang, idSanPham, "active");
            if (cartItem.isEmpty()) {
                result.put("status", false);
                result.put("message", "Không tìm thấy sản phẩm trong giỏ hàng");
                return result;
            }
            
            // Kiểm tra mã giảm giá có áp dụng được cho sản phẩm này không
            if (!discountService.isDiscountApplicableToProduct(discount.getId(), idSanPham)) {
                result.put("status", false);
                result.put("message", "Mã giảm giá không áp dụng cho sản phẩm này");
                return result;
            }
            
            carts = List.of(cartItem.get());
        } else {
            // Nếu không có idSanPham, áp dụng cho toàn bộ giỏ hàng
            carts = cartRepository.findByIdKhachHangAndTrangThai(idKhachHang, "active");
            if (carts.isEmpty()) {
                result.put("status", false);
                result.put("message", "Giỏ hàng trống");
                return result;
            }
        }
        
        // Tính toán giảm giá
        double totalBeforeDiscount = 0;
        double totalAfterDiscount = 0;
        double discountAmount = 0;
        
        for (Cart cart : carts) {
            double itemTotal = cart.getThanhTien();
            totalBeforeDiscount += itemTotal;
            
            // Kiểm tra mã giảm giá có áp dụng được cho sản phẩm này không
            if (discountService.isDiscountApplicableToProduct(discount.getId(), cart.getIdSanPham())) {
                // Tính giảm giá theo phần trăm
                double itemDiscount = itemTotal * discount.getPhamTramGiamGia() / 100;
                
                // Kiểm tra nếu có mức giảm tối đa
                if (discount.getThanhTien() != null && discount.getThanhTien() > 0 && itemDiscount > discount.getThanhTien()) {
                    itemDiscount = discount.getThanhTien();
                }
                
                double itemAfterDiscount = itemTotal - itemDiscount;
                totalAfterDiscount += itemAfterDiscount;
                discountAmount += itemDiscount;
            } else {
                // Không áp dụng giảm giá cho sản phẩm này
                totalAfterDiscount += itemTotal;
            }
        }
        
        // Trả về kết quả
        result.put("status", true);
        result.put("message", "Áp dụng mã giảm giá thành công");
        result.put("maGiamGia", maGiamGia);
        result.put("phanTramGiamGia", discount.getPhamTramGiamGia());
        result.put("totalBeforeDiscount", totalBeforeDiscount);
        result.put("totalAfterDiscount", totalAfterDiscount);
        result.put("discountAmount", discountAmount);
        result.put("idGiamGia", discount.getId());
        
        return result;
    }

    /**
     * Lấy giỏ hàng theo ID đơn hàng kèm thông tin sản phẩm
     */
    public List<CartWithProductDto> getCartsWithProductInfoByOrderId(Long idDonHang) {
        // Kiểm tra xem có tồn tại đơn hàng với id này không
        List<Cart> carts = cartRepository.findByIdDonHang(idDonHang);
        
        if (carts == null || carts.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng với id: " + idDonHang);
        }
        
        List<CartWithProductDto> result = new ArrayList<>();
        
        for (Cart cart : carts) {
            // Kiểm tra xem cart có thuộc về đơn hàng này không
            if (!cart.getIdDonHang().equals(idDonHang)) {
                continue;
            }
            
            CartWithProductDto dto = new CartWithProductDto();
            
            // Set cart info
            dto.setId(cart.getId());
            dto.setIdKhachHang(cart.getIdKhachHang());
            dto.setIdSanPham(cart.getIdSanPham());
            dto.setIdDonHang(cart.getIdDonHang());
            dto.setSoLuong(cart.getSoLuong());
            dto.setThanhTien(cart.getThanhTien());
            dto.setTrangThai(cart.getTrangThai());
            dto.setNgayTao(cart.getNgayTao());
            dto.setNgayCapNhat(cart.getNgayCapNhat());
            
            // Get product info
            Product product = productRepository.findByIdWithJoins(cart.getIdSanPham());
            if (product != null) {
                dto.setTenSanPham(product.getTenSanPham());
                dto.setGiaSanPham(product.getGiaSanPham());
                dto.setKichCo(product.getKichCo());
                dto.setMauSac(product.getMauSac());
                dto.setChatLieu(product.getChatLieu());
                dto.setHinhAnh(product.getHinhAnh());
                dto.setSoLuongTonKho(product.getSoLuongTonKho());
                
                if (product.getDanhMuc() != null) {
                    dto.setIdDanhMuc(product.getDanhMuc().getId());
                    dto.setTenDanhMuc(product.getDanhMuc().getName());
                }
                
                if (product.getThuongHieu() != null) {
                    dto.setIdThuongHieu(product.getThuongHieu().getId());
                    dto.setTenThuongHieu(product.getThuongHieu().getTenThuongHieu());
                }
                
                if (product.getGiamGia() != null) {
                    dto.setIdGiamGia(product.getGiamGia().getId());
                    dto.setPhanTramGiamGia(product.getGiamGia().getPhamTramGiamGia().intValue());
                    dto.setMaGiamGia(product.getGiamGia().getMaCode());
                }
            }
            
            result.add(dto);
        }
        
        if (result.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm nào trong đơn hàng với id: " + idDonHang);
        }
        
        return result;
    }

    /**
     * Lấy tất cả giỏ hàng kèm thông tin sản phẩm (cho admin)
     */
    public List<CartWithProductDto> getAllCartsWithProductInfo() {
        List<Cart> carts = cartRepository.findAll();
        List<CartWithProductDto> result = new ArrayList<>();
        
        for (Cart cart : carts) {
            CartWithProductDto dto = new CartWithProductDto();
            
            // Set cart info
            dto.setId(cart.getId());
            dto.setIdKhachHang(cart.getIdKhachHang());
            dto.setIdSanPham(cart.getIdSanPham());
            dto.setIdDonHang(cart.getIdDonHang());
            dto.setSoLuong(cart.getSoLuong());
            dto.setThanhTien(cart.getThanhTien());
            dto.setTrangThai(cart.getTrangThai());
            dto.setNgayTao(cart.getNgayTao());
            dto.setNgayCapNhat(cart.getNgayCapNhat());
            
            // Get product info
            Product product = productRepository.findByIdWithJoins(cart.getIdSanPham());
            if (product != null) {
                dto.setTenSanPham(product.getTenSanPham());
                dto.setGiaSanPham(product.getGiaSanPham());
                dto.setKichCo(product.getKichCo());
                dto.setMauSac(product.getMauSac());
                dto.setChatLieu(product.getChatLieu());
                dto.setHinhAnh(product.getHinhAnh());
                dto.setSoLuongTonKho(product.getSoLuongTonKho());
                
                if (product.getDanhMuc() != null) {
                    dto.setIdDanhMuc(product.getDanhMuc().getId());
                    dto.setTenDanhMuc(product.getDanhMuc().getName());
                }
                
                if (product.getThuongHieu() != null) {
                    dto.setIdThuongHieu(product.getThuongHieu().getId());
                    dto.setTenThuongHieu(product.getThuongHieu().getTenThuongHieu());
                }
                
                if (product.getGiamGia() != null) {
                    dto.setIdGiamGia(product.getGiamGia().getId());
                    dto.setPhanTramGiamGia(product.getGiamGia().getPhamTramGiamGia().intValue());
                    dto.setMaGiamGia(product.getGiamGia().getMaCode());
                }
            }
            
            result.add(dto);
        }
        
        return result;
    }

    // /**
    //  * Cập nhật trạng thái đơn hàng
    //  */
    // public void updateTrangThaiDonHang(Long idDonHang, String trangThaiMoi) {
    //     List<Cart> carts = cartRepository.findByIdDonHang(idDonHang);
    //     if (carts.isEmpty()) {
    //         throw new RuntimeException("Không tìm thấy đơn hàng với id: " + idDonHang);
    //     }
        
    //     for (Cart cart : carts) {
    //         cart.setTrangThai(trangThaiMoi);
    //         cartRepository.save(cart);
    //     }
    // }

    // /**
    //  * Cập nhật địa chỉ giao hàng
    //  */
    // public void updateDiaChiGiaoHang(Long idDonHang, String diaChiMoi) {
    //     List<Cart> carts = cartRepository.findByIdDonHang(idDonHang);
    //     if (carts.isEmpty()) {
    //         throw new RuntimeException("Không tìm thấy đơn hàng với id: " + idDonHang);
    //     }
        
    //     // // Lưu ý: Cần thêm trường diaChiGiaoHang vào entity Cart nếu chưa có
    //     // for (Cart cart : carts) {
    //     //     cart.setDiaChiGiaoHang(diaChiMoi);
    //     //     cartRepository.save(cart);
    //     // }
    // }

    // /**
    //  * Cập nhật phương thức thanh toán
    //  */
    // public void updatePhuongThucThanhToan(Long idDonHang, String phuongThucMoi) {
    //     List<Cart> carts = cartRepository.findByIdDonHang(idDonHang);
    //     if (carts.isEmpty()) {
    //         throw new RuntimeException("Không tìm thấy đơn hàng với id: " + idDonHang);
    //     }
        
    //     // // Lưu ý: Cần thêm trường phuongThucThanhToan vào entity Cart nếu chưa có
    //     // for (Cart cart : carts) {
    //     //     cart.setPhuongThucThanhToan(phuongThucMoi);
    //     //     cartRepository.save(cart);
    //     // }
    // }

    // /**
    //  * Xóa đơn hàng
    //  */
    // public void deleteDonHang(Long idDonHang) {
    //     List<Cart> carts = cartRepository.findByIdDonHang(idDonHang);
    //     if (carts.isEmpty()) {
    //         throw new RuntimeException("Không tìm thấy đơn hàng với id: " + idDonHang);
    //     }
        
    //     cartRepository.deleteAll(carts);
    // }

    /**
     * Áp dụng mã giảm giá cho đơn hàng
     * 
     * @param idDonHang id của đơn hàng
     * @param maGiamGia mã giảm giá người dùng nhập vào
     * @param tongTien tổng tiền của đơn hàng
     * @return Thông tin kết quả áp dụng mã giảm giá
     */
    public Map<String, Object> applyDiscountCodeToOrder(Long idDonHang, String maGiamGia, Double tongTien) {
        Map<String, Object> result = new HashMap<>();
        
        // Kiểm tra mã giảm giá có tồn tại và hợp lệ không
        Optional<Discount> discountOpt = discountService.findValidDiscount(maGiamGia);
        if (discountOpt.isEmpty()) {
            result.put("status", false);
            result.put("message", "Mã giảm giá không hợp lệ hoặc đã hết hạn");
            return result;
        }
        
        Discount discount = discountOpt.get();
        
        // Tính toán giảm giá
        double discountAmount = tongTien * discount.getPhamTramGiamGia() / 100;
        
        // Kiểm tra nếu có mức giảm tối đa
        if (discount.getThanhTien() != null && discount.getThanhTien() > 0 && discountAmount > discount.getThanhTien()) {
            discountAmount = discount.getThanhTien();
        }
        
        double totalAfterDiscount = tongTien - discountAmount;
        
        // Trả về kết quả
        result.put("status", true);
        result.put("message", "Áp dụng mã giảm giá thành công");
        result.put("data", Map.of(
            "maGiamGia", maGiamGia,
            "phanTramGiamGia", discount.getPhamTramGiamGia(),
            "totalBeforeDiscount", tongTien,
            "totalAfterDiscount", totalAfterDiscount,
            "discountAmount", discountAmount,
            "idGiamGia", discount.getId(),
            "giaTriGiamToiDa", discount.getThanhTien()
        ));
        
        return result;
    }

    /**
     * Tạo đơn hàng mới
     */
    
    public void updateCartStatus(Long idKhachHang, Long idSanPham, String status) {
        Optional<Cart> cartOpt = cartRepository.findByIdKhachHangAndIdSanPhamAndTrangThai(idKhachHang, idSanPham, "active");
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.setTrangThai(status);
            cartRepository.save(cart);
        }
    }
}
