package com.fpt_be.fpt_be.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fpt_be.fpt_be.Security.auth.TokenType;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.expiration-time}")
    private int jwtExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Phương thức tạo token cũ, giữ lại để tương thích ngược
     * @deprecated Sử dụng generateUserToken() hoặc generateAdminToken() thay thế
     */
    public String generateToken(Long userId, String email) {
        return generateUserToken(userId, email);
    }

    /**
     * Tạo token cho khách hàng (USER)
     */
    public String generateUserToken(Long userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("tokenType", TokenType.USER.name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Tạo token cho Admin
     */
    public String generateAdminToken(Long adminId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(String.valueOf(adminId))
                .claim("email", email)
                .claim("tokenType", TokenType.ADMIN.name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Kiểm tra tính hợp lệ của token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Lấy ID người dùng từ token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
    
    /**
     * Lấy loại token (USER hoặc ADMIN)
     */
    public TokenType getTokenType(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    
            String tokenTypeStr = claims.get("tokenType", String.class);
            if (tokenTypeStr != null) {
                return TokenType.valueOf(tokenTypeStr);
            }
            
            // Cho các token cũ không có tokenType, mặc định là USER
            return TokenType.USER;
        } catch (Exception ex) {
            return null;
        }
    }
    
    /**
     * Kiểm tra xem token có phải là token admin hay không
     */
    public boolean isAdminToken(String token) {
        TokenType type = getTokenType(token);
        return type == TokenType.ADMIN;
    }
    
    /**
     * Kiểm tra xem token có phải là token user hay không
     */
    public boolean isUserToken(String token) {
        TokenType type = getTokenType(token);
        return type == TokenType.USER;
    }
} 