package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.DExchange;
import com.example.aadbackspring.service.DExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;

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
        List<DExchange> exchanges = service.getAllDExchanges();
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

    // This endpoint simulates the external DExchange API response structure.
    @GetMapping("/test-api")
    public ResponseEntity<?> getTestApiResponse() {
        List<DExchange> exchanges = service.getAllDExchanges();
        Map<String, Object> response = new HashMap<>();
        response.put("data", exchanges);
        Map<String, Object> status = new HashMap<>();
        status.put("timestamp", OffsetDateTime.now().toString());
        status.put("error_code", "0");
        status.put("error_message", "");
        status.put("elapsed", 45);
        status.put("credit_count", 50);
        response.put("status", status);
        return ResponseEntity.ok(response);
    }
}
