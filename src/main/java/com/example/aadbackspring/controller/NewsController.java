package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.News;
import com.example.aadbackspring.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<News> getAllNews() {
        return newsService.getNews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        News news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News createdNews = newsService.createNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News newsDetails) {
        News updatedNews = newsService.updateNews(id, newsDetails);
        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok("{\"message\": \"News with id " + id + " deleted successfully\"}");
    }
}
