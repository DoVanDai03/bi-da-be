package com.fpt_be.fpt_be.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fpt_be.fpt_be.Dto.NewsDto;
import com.fpt_be.fpt_be.Entity.news;
import com.fpt_be.fpt_be.Service.NewsService;

@RestController
@RequestMapping("/api/news")
public class HomeNewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping
    public ResponseEntity<?> getAllNews() {
        try {
            List<news> news = newsService.getAllNews();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách bài viết thành công",
                            "data", news));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    } 

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable Long id) {
        try {
            news news = newsService.getNewsById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy bài viết thành công",
                            "data", news));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody NewsDto newsDto) {
        try {
            news news = newsService.createNews(newsDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", true,
                            "message", "Thêm bài viết thành công",
                            "data", news));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNews(@PathVariable Long id, @RequestBody NewsDto newsDto) {
        try {
            news news = newsService.updateNews(id, newsDto);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật bài viết thành công",
                            "data", news));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        try {
            newsService.deleteNews(id);
            return ResponseEntity.ok()
                    .body(Map.of("status", true, "message", "Xóa bài viết thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
}
