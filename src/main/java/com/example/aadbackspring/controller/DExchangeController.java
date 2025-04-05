package com.example.aadbackspring.controller;

import com.example.aadbackspring.exception.ResourceNotFoundException;
import com.example.aadbackspring.model.DExchange;
import com.example.aadbackspring.service.DExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/dexchanges")
public class DExchangeController {

    private final DExchangeService service;

    public DExchangeController(DExchangeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createDExchange(@RequestBody DExchange dexchange) {
        try {
            DExchange created = service.createDExchange(dexchange);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<DExchange>> getAllDExchanges() {
        List<DExchange> exchanges = service.getDexListings();
        return ResponseEntity.ok(exchanges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DExchange> getDExchangeById(@PathVariable Long id) {
        DExchange exchange = service.getDExchangeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DExchange not found with id " + id));
        return ResponseEntity.ok(exchange);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DExchange> updateDExchange(@PathVariable Long id, @RequestBody DExchange dexchange) {
        DExchange updatedExchange = service.updateDExchange(id, dexchange)
                .orElseThrow(() -> new ResourceNotFoundException("DExchange not found with id " + id));
        return ResponseEntity.ok(updatedExchange);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDExchange(@PathVariable Long id) {
        boolean deleted = service.deleteDExchange(id);
        if (!deleted) {
            throw new ResourceNotFoundException("DExchange not found with id " + id);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "DExchange deleted successfully"));
    }

}
