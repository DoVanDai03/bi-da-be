package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Dto.NewsDto;
import com.fpt_be.fpt_be.Entity.news;
import com.fpt_be.fpt_be.Repository.NewsRepository;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    public List<news> getAllNews() {
        return newsRepository.findAll();
    }

    public news createNews(NewsDto newsDto) {
        news news = new news();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setImage(newsDto.getImage());
        news.setStatus(newsDto.getStatus());
        news.setAuthor(newsDto.getAuthor());
        return newsRepository.save(news);
    }

    public news updateNews(Long id, NewsDto newsDto) {
        Optional<news> existingNews = newsRepository.findById(id);
        if (existingNews.isPresent()) {
            news news = existingNews.get();
            news.setTitle(newsDto.getTitle());
            news.setContent(newsDto.getContent());
            news.setImage(newsDto.getImage());
            news.setStatus(newsDto.getStatus());
            news.setAuthor(newsDto.getAuthor());
            return newsRepository.save(news);
        }
        throw new RuntimeException("Không tìm thấy bài viết với id: " + id);
    }

    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bài viết với id: " + id);
        }
        newsRepository.deleteById(id);
    }

    public news getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với id: " + id));
    }
}
