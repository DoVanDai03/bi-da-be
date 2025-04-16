package com.fpt_be.fpt_be.Controller;

import com.fpt_be.fpt_be.Request.DoiMatKhauRequest;
import com.fpt_be.fpt_be.Request.QuenMatKhauRequest;
import com.fpt_be.fpt_be.Service.QuenMatKhauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quen-mat-khau")
public class QuenMatKhauController {

    @Autowired
    private QuenMatKhauService quenMatKhauService;

    @PostMapping("/xac-nhan-ma")
    public ResponseEntity<?> xacNhanMa(@RequestBody QuenMatKhauRequest request) {
        return quenMatKhauService.xacNhanMa(request);
    }

    @PostMapping("/doi-mat-khau")
    public ResponseEntity<?> doiMatKhau(@RequestBody DoiMatKhauRequest request) {
        return quenMatKhauService.doiMatKhau(request);
    }
} 