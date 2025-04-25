package com.fpt_be.fpt_be.Validator;

import com.fpt_be.fpt_be.Request.DoiMatKhauRequest;
import com.fpt_be.fpt_be.Request.QuenMatKhauRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class QuenMatKhauValidator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static List<String> validateDoiMatKhau(DoiMatKhauRequest request) {
        List<String> errors = new ArrayList<>();

        // Validate email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.add("Email không được để trống");
        } else if (!Pattern.matches(EMAIL_PATTERN, request.getEmail())) {
            errors.add("Email không hợp lệ");
        }

        // Validate mật khẩu mới
        if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            errors.add("Mật khẩu mới không được để trống");
        } else if (!Pattern.matches(PASSWORD_PATTERN, request.getNewPassword())) {
            errors.add("Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
        }

        return errors;
    }

    public static List<String> validateQuenMatKhau(QuenMatKhauRequest request) {
        List<String> errors = new ArrayList<>();

        // Validate email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.add("Email không được để trống");
        } else if (!Pattern.matches(EMAIL_PATTERN, request.getEmail())) {
            errors.add("Email không hợp lệ");
        }

        // Validate mã xác nhận nếu có
        if (request.getMaXacNhan() != null && request.getMaXacNhan().trim().isEmpty()) {
            errors.add("Mã xác nhận không được để trống");
        }

        return errors;
    }
} 