package com.example.aadbackspring.service;


import com.example.aadbackspring.model.Article;
import com.example.aadbackspring.model.Category;
import com.example.aadbackspring.repository.ArticleRepository;
import com.example.aadbackspring.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public ArticleService(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    public Article createArticle(Article article) {
        // If a category is passed (even only with an id), reattach the managed category.
        if (article.getCategory() != null && article.getCategory().getId() != null) {
            Category managedCategory = categoryRepository.findById(article.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            article.setCategory(managedCategory);
        }
        return articleRepository.save(article);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public Optional<Article> updateArticle(Long id, Article articleData) {
        return articleRepository.findById(id).map(article -> {
            article.setTitle(articleData.getTitle());
            article.setContent(articleData.getContent());
            if (articleData.getCategory() != null && articleData.getCategory().getId() != null) {
                Category managedCategory = categoryRepository.findById(articleData.getCategory().getId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                article.setCategory(managedCategory);
            }
            return articleRepository.save(article);
        });
    }

    public boolean deleteArticle(Long id) {
        return articleRepository.findById(id).map(article -> {
            articleRepository.delete(article);
            return true;
        }).orElse(false);
    }
}
