package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.DExchange;
import com.example.aadbackspring.service.DExchangeService;
import com.example.aadbackspring.service.DExApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/dexchanges")
public class DExchangeController {

    private final DExchangeService service;
    private final DExApiService dexApiService;

    // Inject both the local service and the new API service
    public DExchangeController(DExchangeService service, DExApiService dexApiService) {
        this.service = service;
        this.dexApiService = dexApiService;
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
        List<DExchange> exchanges = dexApiService.getDexListings();
        return ResponseEntity.ok(exchanges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDExchangeById(@PathVariable Long id) {
        return service.getDExchangeById(id)
                .map(exchange -> ResponseEntity.ok((Object) exchange))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "DExchange not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDExchange(@PathVariable Long id, @RequestBody DExchange dexchange) {
        try {
            return service.updateDExchange(id, dexchange)
                    .map(updated -> ResponseEntity.ok((Object) updated))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap("error", "DExchange not found")));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDExchange(@PathVariable Long id) {
        boolean deleted = service.deleteDExchange(id);
        if (deleted) {
            return ResponseEntity.ok(Collections.singletonMap("message", "DExchange deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "DExchange not found"));
        }
    }
}
