package com.example.aadbackspring.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "terms")
@Getter
@Setter
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String term;

    @Column(nullable = false, columnDefinition = "text")
    private String meaning;
}