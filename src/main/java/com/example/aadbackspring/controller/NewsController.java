package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.News;
import com.example.aadbackspring.service.NewsService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // GET all news (external API with fallback)
    @GetMapping
    public List<News> getAllNews() {
        return newsService.getNews();
    }

    // GET a single news record by id
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        News news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    // CREATE a new news record
    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News createdNews = newsService.createNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    // UPDATE an existing news record
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News newsDetails) {
        News updatedNews = newsService.updateNews(id, newsDetails);
        return ResponseEntity.ok(updatedNews);
    }

    // DELETE a news record by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok("{\"message\": \"News with id " + id + " deleted successfully\"}");
    }

    // GET local news
    @GetMapping("/local")
    public List<News> getLocalNews() {
        return newsService.getLocalNews();
    }
}
