package com.example.aadbackspring.service;

import com.example.aadbackspring.model.DExchange;
import com.example.aadbackspring.repository.DExchangeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DExchangeService {

    private final DExchangeRepository repository;

    public DExchangeService(DExchangeRepository repository) {
        this.repository = repository;
    }

    public DExchange createDExchange(DExchange dexchange) {
        return repository.save(dexchange);
    }

    public List<DExchange> getAllDExchanges() {
        return repository.findAll();
    }

    public Optional<DExchange> getDExchangeById(Long id) {
        return repository.findById(id);
    }

    public Optional<DExchange> updateDExchange(Long id, DExchange exchangeDetails) {
        return repository.findById(id).map(existing -> {
            existing.setExternalId(exchangeDetails.getExternalId());
            existing.setNumMarketPairs(exchangeDetails.getNumMarketPairs());
            existing.setLastUpdated(exchangeDetails.getLastUpdated());
            existing.setMarketShare(exchangeDetails.getMarketShare());
            existing.setType(exchangeDetails.getType());
            existing.setQuote(exchangeDetails.getQuote());
            existing.setName(exchangeDetails.getName());
            existing.setSlug(exchangeDetails.getSlug());
            existing.setStatus(exchangeDetails.getStatus());
            return repository.save(existing);
        });
    }

    public boolean deleteDExchange(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
