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

@RestController
@RequestMapping("/api/admin/chuc-vu")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping
    public ResponseEntity<?> getAllPositions() {
        try {
            List<Position> positions = positionService.getAllPositions();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách chức vụ thành công",
                            "data", positions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPositionById(@PathVariable Long id) {
        try {
            Position position = positionService.getPositionById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin chức vụ thành công",
                            "data", position));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPosition(@RequestBody PositionDto positionDto) {
        try {
            Position createdPosition = positionService.createPosition(positionDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", true,
                            "message", "Thêm chức vụ thành công",
                            "data", createdPosition));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePosition(@PathVariable Long id, @RequestBody PositionDto positionDto) {
        try {
            Position updatedPosition = positionService.updatePosition(id, positionDto);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật chức vụ thành công",
                            "data", updatedPosition));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePosition(@PathVariable Long id) {
        try {
            positionService.deletePosition(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa chức vụ thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 