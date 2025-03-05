package com.example.aadbackspring.service;

import com.example.aadbackspring.model.News;
import com.example.aadbackspring.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository repository;

    public NewsService(NewsRepository repository) {
        this.repository = repository;
    }

    public News createNews(News news) {
        return repository.save(news);
    }

    public List<News> getAllNews() {
        return repository.findAll();
    }

    public Optional<News> getNewsById(Long id) {
        return repository.findById(id);
    }

    public Optional<News> updateNews(Long id, News newsDetails) {
        return repository.findById(id).map(existing -> {
            existing.setType(newsDetails.getType());
            existing.setSourceKey(newsDetails.getSourceKey());
            existing.setName(newsDetails.getName());
            existing.setImageUrl(newsDetails.getImageUrl());
            existing.setUrl(newsDetails.getUrl());
            existing.setLang(newsDetails.getLang());
            existing.setSourceType(newsDetails.getSourceType());
            existing.setLaunchDate(newsDetails.getLaunchDate());
            existing.setSortOrder(newsDetails.getSortOrder());
            existing.setBenchmarkScore(newsDetails.getBenchmarkScore());
            existing.setStatus(newsDetails.getStatus());
            existing.setLastUpdatedTs(newsDetails.getLastUpdatedTs());
            existing.setCreatedOn(newsDetails.getCreatedOn());
            existing.setUpdatedOn(newsDetails.getUpdatedOn());
            return repository.save(existing);
        });
    }

    public boolean deleteNews(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
