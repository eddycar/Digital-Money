package com.dh.accountservice.repository.feing;

import com.dh.accountservice.entities.Card;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "cards-service", url = "http://localhost:8085")
public interface ICardFeignRepository {
    @PostMapping("/cards")
    ResponseEntity<Card> saveCard(@RequestBody Card card);

    @GetMapping("/cards/accounts/{accountId}")
    Optional<List<Card>> findAllByAccountId(@PathVariable Long accountId);

    @GetMapping("/cards/{cardId}")
    Optional<Card> findCardById(@PathVariable Long cardId);

    @DeleteMapping("/cards/accounts/{cardId}")
    ResponseEntity<String> deleteCardById(@PathVariable Long cardId);
}

