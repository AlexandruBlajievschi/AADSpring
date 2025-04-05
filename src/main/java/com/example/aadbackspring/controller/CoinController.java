package com.example.aadbackspring.controller;

import com.example.aadbackspring.exception.ResourceNotFoundException;
import com.example.aadbackspring.model.Coin;
import com.example.aadbackspring.service.CoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @PostMapping
    public ResponseEntity<Coin> createCoin(@RequestBody Coin coin) {
        Coin created = coinService.createCoin(coin);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Coin>> getAllCoins() {
        List<Coin> coins = coinService.getCoinListings();
        return ResponseEntity.ok(coins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coin> getCoinById(@PathVariable Long id) {
        Coin coin = coinService.getCoinById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coin not found with id " + id));
        return ResponseEntity.ok(coin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coin> updateCoin(@PathVariable Long id, @RequestBody Coin coin) {
        Coin updated = coinService.updateCoin(id, coin);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoin(@PathVariable Long id) {
        boolean deleted = coinService.deleteCoin(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Coin not found with id " + id);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Coin deleted successfully"));
    }
}
