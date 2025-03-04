package com.example.aadbackspring.service;

import com.example.aadbackspring.model.Term;
import com.example.aadbackspring.repository.TermRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TermService {

    private final TermRepository termRepository;

    public TermService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public Term createTerm(Term term) {
        return termRepository.save(term);
    }

    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }

    public Optional<Term> getTermById(Long id) {
        return termRepository.findById(id);
    }

    public Term updateTerm(Long id, Term newTermData) {
        return termRepository.findById(id).map(term -> {
            term.setTerm(newTermData.getTerm());
            term.setMeaning(newTermData.getMeaning());
            return termRepository.save(term);
        }).orElse(null);
    }

    public boolean deleteTerm(Long id) {
        return termRepository.findById(id).map(term -> {
            termRepository.delete(term);
            return true;
        }).orElse(false);
    }
}