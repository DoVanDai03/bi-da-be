package com.fpt_be.fpt_be.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.fpt_be.fpt_be.Request.DangKyRequest;

public class UserValidator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_PATTERN = "^0\\d{9}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String NAME_PATTERN = "^[\\p{L} .'-]+$";

    public static List<String> validateDangKy(DangKyRequest request) {
        List<String> errors = new ArrayList<>();

        // Validate email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.add("Email không được để trống");
        } else if (!Pattern.matches(EMAIL_PATTERN, request.getEmail())) {
            errors.add("Email không hợp lệ");
        }

        // Validate password
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            errors.add("Mật khẩu không được để trống");
        } else if (!Pattern.matches(PASSWORD_PATTERN, request.getPassword())) {
            errors.add("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
        }

        // Validate họ và tên
        if (request.getHoVaTen() == null || request.getHoVaTen().trim().isEmpty()) {
            errors.add("Họ và tên không được để trống");
        } else if (!Pattern.matches(NAME_PATTERN, request.getHoVaTen())) {
            errors.add("Họ và tên không được chứa ký tự đặc biệt");
        }

        // Validate số điện thoại
        if (request.getSdt() == null || request.getSdt().trim().isEmpty()) {
            errors.add("Số điện thoại không được để trống");
        } else if (!Pattern.matches(PHONE_PATTERN, request.getSdt())) {
            errors.add("Số điện thoại không hợp lệ (phải có 10 số và bắt đầu bằng số 0)");
        }

        return errors;
    }
} 