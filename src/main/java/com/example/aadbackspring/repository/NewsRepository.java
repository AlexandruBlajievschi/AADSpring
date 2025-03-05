package com.example.aadbackspring.repository;

import com.example.aadbackspring.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // Additional query methods can be defined here if needed.
}
