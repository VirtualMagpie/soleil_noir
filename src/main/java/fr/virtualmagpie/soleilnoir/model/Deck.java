package fr.virtualmagpie.soleilnoir.model;

import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.card.CardProvider;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Deck {
  private final LinkedList<Card> cards = new LinkedList<>();

  /**
   * Init deck with cards
   *
   * @return number of cards in deck.
   */
  public int init() {
    this.clear();
    for (int i = 0; i < CardProvider.length(); i++) {
      cards.add(CardProvider.get(i));
    }
    return cards.size();
  }

  /** Empty deck */
  public void clear() {
    cards.clear();
  }

  /** Shuffle deck */
  public void shuffle(Random random) {
    Collections.shuffle(cards, random);
  }

  /** See next cards. Do not alter deck. */
  public Card[] seeNext(int nbCard) {
    if (nbCard <= 0 || nbCard > cards.size()) {
      throw new IllegalArgumentException("Invalid number of cards");
    }
    Card[] seen = new Card[nbCard];
    for (int i = 0; i < nbCard; i++) {
      seen[i] = cards.get(i);
    }
    return seen;
  }

  /** Draw a card, remove it from the deck */
  public Card draw() {
    return cards.pop();
  }

  /** Draw cards, remove them from the deck */
  public Card[] draw(int nbCard) {
    if (nbCard <= 0 || nbCard > cards.size()) {
      throw new IllegalArgumentException("Invalid number of cards");
    }
    Card[] drawn = new Card[nbCard];
    for (int i = 0; i < nbCard; i++) {
      drawn[i] = this.draw();
    }
    return drawn;
  }

  /** Add card under the deck. */
  public void add(Card cardToAdd) {
    cards.addLast(cardToAdd);
  }

  /** Add cards under the deck. */
  public void add(Card[] cardsToAdd) {
    for (Card cardToAdd : cardsToAdd) {
      this.add(cardToAdd);
    }
  }
}
