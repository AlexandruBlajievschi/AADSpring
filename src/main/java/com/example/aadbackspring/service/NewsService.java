package com.example.aadbackspring.service;

import com.example.aadbackspring.dto.NewsApiResponseDTO;
import com.example.aadbackspring.exception.ResourceNotFoundException;
import com.example.aadbackspring.model.News;
import com.example.aadbackspring.repository.NewsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    private final RestTemplate restTemplate;
    private final NewsRepository newsRepository;
    @Autowired
    private AppConfigurationService configService;

    @Value("${external.news.api.url}")
    private String externalNewsApiUrl;

    public NewsService(RestTemplateBuilder restTemplateBuilder, NewsRepository newsRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.newsRepository = newsRepository;
    }

    /**
     * Calls the external News API and returns a list of News.
     * If the external API call fails or returns no data, a ResourceNotFoundException is thrown.
     */
    @CircuitBreaker(name = "cryptocompare", fallbackMethod = "newsApiFallback")
    public List<News> getNewsFromExternal() {
        ResponseEntity<NewsApiResponseDTO> response = restTemplate.getForEntity(externalNewsApiUrl, NewsApiResponseDTO.class);
        if (response.getStatusCode().is2xxSuccessful() &&
                response.getBody() != null &&
                response.getBody().getData() != null) {
            return response.getBody().getData();
        } else {
            throw new ResourceNotFoundException("External News API resource not found");
        }
    }

    /**
     * Fallback method for the circuit breaker.
     * Logs the error and returns local data from the repository.
     */
    public List<News> newsApiFallback(Throwable t) {
        logger.error("External News API call failed: {}", t.getMessage());
        return newsRepository.findAll();
    }

    /**
     * Unified method that attempts to fetch news from the external API.
     * If that fails, it falls back to local repository data.
     */
    public List<News> getNews() {
        boolean useLocal = configService.getConfig().isUseLocalNews();

        if (useLocal) {
            return newsRepository.findAll();
        } else {
            // Fallback if external fails
            try {
                return getNewsFromExternal();
            } catch (Exception ex) {
                logger.warn("Falling back to local news: {}", ex.getMessage());
                return newsRepository.findAll();
            }
        }
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
     * The task is marked as @Transactional to ensure data consistency.
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updateLocalNews() {
        try {
            logger.info("Running scheduled task to update local news.");
            List<News> externalNews = getNewsFromExternal();
            // Optionally clear old news:
            newsRepository.deleteAll();
            newsRepository.saveAll(externalNews);
            logger.info("Local news updated successfully with {} records.", externalNews.size());
        } catch (Exception e) {
            logger.error("Scheduled update failed: {}", e.getMessage());
        }
    }
}
