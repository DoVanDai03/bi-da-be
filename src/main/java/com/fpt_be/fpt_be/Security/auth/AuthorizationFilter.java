package com.fpt_be.fpt_be.Security.auth;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt_be.fpt_be.Security.JwtTokenProvider;

/**
 * Filter kiểm tra quyền truy cập dựa trên loại token
 */
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (shouldNotFilter(request)) {
                // Nếu là endpoint không cần xác thực, bỏ qua filter
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // Token hợp lệ, kiểm tra quyền truy cập dựa trên loại token
                String path = request.getRequestURI();

                if (isAdminEndpoint(path)) {
                    // Nếu là admin endpoint, phải có token admin
                    if (jwtTokenProvider.isAdminToken(jwt)) {
                        // Là token admin, cho phép truy cập
                        Long adminId = jwtTokenProvider.getUserIdFromToken(jwt);
                        request.setAttribute("adminId", adminId);
                        filterChain.doFilter(request, response);
                    } else {
                        // Không phải token admin, từ chối quyền truy cập
                        sendForbiddenResponse(response, "Yêu cầu quyền quản trị viên");
                    }
                } else {
                    // Nếu là user endpoint, có thể dùng token user hoặc admin đều được
                    // Hoặc có thể thay đổi để chỉ cho phép token user truy cập vào user endpoint
                    Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                    request.setAttribute("userId", userId);
                    
                    // Thêm thuộc tính tokenType để các controller có thể sử dụng
                    TokenType tokenType = jwtTokenProvider.getTokenType(jwt);
                    request.setAttribute("tokenType", tokenType);
                    
                    filterChain.doFilter(request, response);
                }
            } else {
                // Token không hợp lệ
                sendUnauthorizedResponse(response, "Token không hợp lệ");
            }
        } catch (Exception ex) {
            // Lỗi xác thực
            sendUnauthorizedResponse(response, "Lỗi xác thực: " + ex.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Các endpoint không cần xác thực
        if (path.startsWith("/api/dang-nhap") || 
            path.startsWith("/api/admin/dang-nhap") || 
            path.startsWith("/api/dang-ky") || 
            path.startsWith("/api/quen-mat-khau") ||
            path.startsWith("/api/user/kiem-tra-token") ||
            path.startsWith("/api/admin/dang-xuat") ||  // Thêm endpoint đăng xuất admin
            path.startsWith("/api/admin/kiem-tra-token") ||
            path.startsWith("/api/user/") // Cho phép truy cập tất cả các endpoint của danh mục
            ) {
            return true;
        }
        
        // Endpoint chi tiết sản phẩm không cần xác thực
        if (path.matches("/api/san-pham/\\d+")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Kiểm tra xem đường dẫn có phải là admin endpoint hay không
     */
    private boolean isAdminEndpoint(String path) {
        return path.startsWith("/api/admin/");
    }

    /**
     * Lấy JWT token từ header Authorization
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * Gửi phản hồi 401 Unauthorized
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), new ErrorResponse(message));
    }
    
    /**
     * Gửi phản hồi 403 Forbidden
     */
    private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), new ErrorResponse(message));
    }

    /**
     * Lớp đại diện cho phản hồi lỗi
     */
    private static class ErrorResponse {
        private boolean status = false;
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public boolean isStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
} 