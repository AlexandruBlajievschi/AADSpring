package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.News;
import com.example.aadbackspring.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody News news) {
        try {
            News created = service.createNews(news);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = service.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getNewsById(@PathVariable Long id) {
        return service.getNewsById(id)
                .map(news -> ResponseEntity.ok((Object) news))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "News not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateNews(@PathVariable Long id, @RequestBody News news) {
        try {
            return service.updateNews(id, news)
                    .map(updatedNews -> ResponseEntity.ok((Object) updatedNews))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap("error", "News not found")));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        boolean deleted = service.deleteNews(id);
        if (deleted) {
            return ResponseEntity.ok(Collections.singletonMap("message", "News deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "News not found"));
        }
    }

    // This endpoint simulates the external API response structure.
    // It returns a JSON object with a "Data" key (listing the news) and an empty "Err" object.
    @GetMapping("/test-api")
    public ResponseEntity<?> getTestApiResponse() {
        List<News> newsList = service.getAllNews();
        Map<String, Object> response = new HashMap<>();
        response.put("Data", newsList);
        response.put("Err", new HashMap<>());
        return ResponseEntity.ok(response);
    }
}
