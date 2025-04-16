package com.fpt_be.fpt_be.Service;

import com.fpt_be.fpt_be.Request.DoiMatKhauRequest;
import com.fpt_be.fpt_be.Request.QuenMatKhauRequest;
import org.springframework.http.ResponseEntity;

public interface QuenMatKhauService {
    ResponseEntity<?> xacNhanMa(QuenMatKhauRequest request);
    ResponseEntity<?> doiMatKhau(DoiMatKhauRequest request);
} 