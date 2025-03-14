package com.example.aadbackspring.service;

import com.example.aadbackspring.dto.NewsApiResponseDTO;
import com.example.aadbackspring.exception.ResourceNotFoundException;
import com.example.aadbackspring.model.News;
import com.example.aadbackspring.repository.NewsRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    private final RestTemplate restTemplate;
    private final NewsRepository newsRepository;

    @Value("${external.news.api.url}")
    private String externalNewsApiUrl;

    public NewsService(RestTemplateBuilder restTemplateBuilder, NewsRepository newsRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.newsRepository = newsRepository;
    }

    /**
     * Returns news from the external API.
     * If the external API call fails, returns the local news from the database.
     */
    public List<News> getNews() {
        try {
            ResponseEntity<NewsApiResponseDTO> response = restTemplate.getForEntity(externalNewsApiUrl, NewsApiResponseDTO.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            } else {
                logger.error("External API returned non-successful response or empty body. Falling back to local news.");
            }
        } catch (Exception e) {
            logger.error("Error calling external news API: {}. Falling back to local news.", e.getMessage());
        }
        return newsRepository.findAll();
    }

    // ---------------------------
    // Additional CRUD Methods
    // ---------------------------

    public News createNews(News news) {
        return newsRepository.save(news);
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id " + id));
    }

    public News updateNews(Long id, News newsDetails) {
        News existingNews = getNewsById(id);
        existingNews.setExternalId(newsDetails.getExternalId());
        existingNews.setGuid(newsDetails.getGuid());
        existingNews.setPublishedOn(newsDetails.getPublishedOn());
        existingNews.setImageurl(newsDetails.getImageurl());
        existingNews.setTitle(newsDetails.getTitle());
        existingNews.setUrl(newsDetails.getUrl());
        existingNews.setBody(newsDetails.getBody());
        existingNews.setTags(newsDetails.getTags());
        existingNews.setLang(newsDetails.getLang());
        existingNews.setCategories(newsDetails.getCategories());
        existingNews.setSource(newsDetails.getSource());
        return newsRepository.save(existingNews);
    }

    public void deleteNews(Long id) {
        News existingNews = getNewsById(id);
        newsRepository.delete(existingNews);
    }

    /**
     * Scheduled task that runs once a day (at midnight) to update local news.
     * Adjust the cron expression as needed.
     */
    @Scheduled(cron = "0 * * * * ?")
    public void updateLocalNews() {
        try {
            logger.info("Running scheduled task to update local news.");
            ResponseEntity<NewsApiResponseDTO> response = restTemplate.getForEntity(externalNewsApiUrl, NewsApiResponseDTO.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                List<News> externalNews = response.getBody().getData();
                // Optionally clear old news:
                newsRepository.deleteAll();
                newsRepository.saveAll(externalNews);
                logger.info("Local news updated successfully with {} records.", externalNews.size());
            } else {
                logger.error("Scheduled update: External API returned non-successful response or empty body.");
            }
        } catch (Exception e) {
            logger.error("Scheduled update failed: {}", e.getMessage());
        }
    }
}
