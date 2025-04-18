package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.PositionDto;
import com.fpt_be.fpt_be.Entity.Position;
import com.fpt_be.fpt_be.Service.PositionService;
import com.fpt_be.fpt_be.Security.JwtTokenProvider;

@RestController
@RequestMapping("/api/admin/chuc-vu")
@CrossOrigin(origins = "*")
public class PositionController {

    @Autowired
    private PositionService positionService;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    private ResponseEntity<?> validateAdminToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok()
                    .body(Map.of("status", false, "message", "Token không hợp lệ"));
        }

        String token = authHeader.substring(7);
        
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.ok()
                    .body(Map.of("status", false, "message", "Token không hợp lệ hoặc đã hết hạn"));
        }
        
        if (!tokenProvider.isAdminToken(token)) {
            return ResponseEntity.ok()
                    .body(Map.of("status", false, "message", "Token không phải là token admin"));
        }

        return null;
    }

    @GetMapping
    public ResponseEntity<?> getAllPositions(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            ResponseEntity<?> validationResult = validateAdminToken(authHeader);
            if (validationResult != null) {
                return validationResult;
            }

            List<Position> positions = positionService.getAllPositions();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách chức vụ thành công",
                            "data", positions));
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPositionById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            ResponseEntity<?> validationResult = validateAdminToken(authHeader);
            if (validationResult != null) {
                return validationResult;
            }

            Position position = positionService.getPositionById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin chức vụ thành công",
                            "data", position));
        } catch (RuntimeException e) {
            return ResponseEntity.ok()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPosition(@RequestBody PositionDto positionDto, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            ResponseEntity<?> validationResult = validateAdminToken(authHeader);
            if (validationResult != null) {
                return validationResult;
            }

            Position createdPosition = positionService.createPosition(positionDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", true,
                            "message", "Thêm chức vụ thành công",
                            "data", createdPosition));
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePosition(@PathVariable Long id, @RequestBody PositionDto positionDto, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            ResponseEntity<?> validationResult = validateAdminToken(authHeader);
            if (validationResult != null) {
                return validationResult;
            }

            Position updatedPosition = positionService.updatePosition(id, positionDto);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật chức vụ thành công",
                            "data", updatedPosition));
        } catch (RuntimeException e) {
            return ResponseEntity.ok()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePosition(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            ResponseEntity<?> validationResult = validateAdminToken(authHeader);
            if (validationResult != null) {
                return validationResult;
            }

            positionService.deletePosition(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa chức vụ thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.ok()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 