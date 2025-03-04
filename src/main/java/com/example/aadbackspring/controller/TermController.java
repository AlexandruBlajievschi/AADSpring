package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.Term;
import com.example.aadbackspring.service.TermService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/terms")
public class TermController {

    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    @PostMapping
    public ResponseEntity<Term> createTerm(@RequestBody Term term) {
        Term created = termService.createTerm(term);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Term>> getAllTerms() {
        List<Term> terms = termService.getAllTerms();
        return ResponseEntity.ok(terms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTermById(@PathVariable Long id) {
        return termService.getTermById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Term not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTerm(@PathVariable Long id, @RequestBody Term term) {
        Term updated = termService.updateTerm(id, term);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Term not found"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTerm(@PathVariable Long id) {
        boolean deleted = termService.deleteTerm(id);
        if (deleted) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Term deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Term not found"));
        }
    }
}