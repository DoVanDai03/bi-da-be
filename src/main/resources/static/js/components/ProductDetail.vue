addToCart() {
    // Kiểm tra xem người dùng đã đăng nhập chưa
    const token_khach_hang = localStorage.getItem('token_khach_hang');
    if (!token_khach_hang) {
        toaster.warning('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!');
        this.$router.push('/dang-nhap');
        return;
    }

    // Kiểm tra các thông tin bắt buộc
    if (!this.selectedSize) {
        toaster.warning('Vui lòng chọn kích thước!');
        return;
    }

    if (!this.selectedColor) {
        toaster.warning('Vui lòng chọn màu sắc!');
        return;
    }

    // Tạo đối tượng sản phẩm cần thêm vào giỏ hàng
    const cartItem = {
        idSanPham: this.san_pham.id,
        soLuong: this.quantity,
        kichCo: this.selectedSize.trim(), // Thêm trim() để loại bỏ khoảng trắng thừa
        mauSac: this.selectedColor.trim(), // Thêm trim() để loại bỏ khoảng trắng thừa
        giaSanPham: this.san_pham.giaSanPham // Sử dụng đúng tên thuộc tính giaSanPham
    };

    // Gọi API thêm vào giỏ hàng với Content-Type: application/json
    axios.post('/api/user/gio-hang', cartItem, {
        headers: {
            'Authorization': `Bearer ${token_khach_hang}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.data.status) {
                toaster.success('Đã thêm sản phẩm vào giỏ hàng thành công!');
            } else {
                toaster.error(response.data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng!');
            }
        })
        .catch(error => {
            console.error('Lỗi khi thêm vào giỏ hàng:', error.response?.data || error);
            if (error.response?.data?.message) {
                toaster.error(error.response.data.message);
            } else {
                toaster.error('Có lỗi xảy ra khi thêm vào giỏ hàng!');
            }
        });
}, 