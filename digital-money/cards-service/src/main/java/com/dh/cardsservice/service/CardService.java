package com.dh.cardsservice.service;

import com.dh.cardsservice.entities.Card;
import com.dh.cardsservice.exceptions.BadRequestException;
import com.dh.cardsservice.exceptions.ConflictException;
import com.dh.cardsservice.exceptions.ResourceNotFoundException;
import com.dh.cardsservice.repository.ICardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CardService {

    @Autowired
    ICardRepository cardRepository;

    public Card saveCard(Card card) throws BadRequestException, ConflictException {
        Optional<Card> existingCard = findByCardNumber(card.getCardNumber());
        if (existingCard.isPresent() && existingCard.get().getAccountId().equals(card.getAccountId())){
           throw new ConflictException("We got a problem the card number it´s already associated with the user account");
        } else if (existingCard.isPresent()) {
            throw new ConflictException("We got a problem the card number it´s already associated with other account");
        }
        if (!validNumeric(card.getCardNumber())){
            throw new BadRequestException("We can´t create the card, the card number must be only numeric characters ");
        } //no valida si se le pasa una cuenta que no existe
        return cardRepository.save(card);
    }
    public Optional<Card> findByCardNumber(String cardNumber){
        return Optional.ofNullable(cardRepository.findByCardNumber(cardNumber));
    }
    public Optional<List<Card>> findAllCardsByAccountId(Long id) throws ResourceNotFoundException {
        Optional<List<Card>> cards = cardRepository.findAllCardsByAccountId(id);
        if (cards.get().isEmpty()){
            throw new ResourceNotFoundException("We did not found any cards associated with the account ID.");
        }else {
            return cards;
        }
    }
    public Card findCardById(Long id) throws ResourceNotFoundException {
       Optional<Card> card = cardRepository.findById(id);
       if (card.isPresent()){
           return card.get();
       }
       throw new ResourceNotFoundException("We don´t found any card associated with the id : " + id);
    }

    public void deleteCardById(Long id) throws BadRequestException {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isPresent()){
           cardRepository.deleteById(id);
        }else {
          throw new BadRequestException("We can´t delete the card with id : " + id +" because the card does not  exist");
        }
        cardRepository.deleteById(id);
    }

    public void updateCardBalanceById(Long id, Card balance) throws ResourceNotFoundException, BadRequestException {
        Optional<Card> card = cardRepository.findById(id);
        if (balance.getBalance() < 0) {
            throw new BadRequestException("Invalid balance value");
        }

        card.get().setBalance(balance.getBalance());
        cardRepository.save(card.get());
    }
    private boolean validNumeric(String number){
        return number.matches("\\d+");
    }
}
