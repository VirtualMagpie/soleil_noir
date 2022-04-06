package fr.virtualmagpie.soleilnoir.model.card;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardProviderTest {

  @Test
  void allCardsExist() {
    List<NormalCard> foundNormalCard = new ArrayList<>();
    List<JokerCard> foundJokerCard = new ArrayList<>();

    for (int i = 0; i < 54; i++) {
      Card card = CardProvider.get(i);
      if (card instanceof NormalCard) {
        foundNormalCard.add((NormalCard) card);
      } else if (card instanceof JokerCard) {
        foundJokerCard.add((JokerCard) card);
      }
    }

    // 52 normal cards
    assertEquals(foundNormalCard.size(), 52);
    // 2 joker cards
    assertEquals(foundJokerCard.size(), 2);

    // all values of normal card
    for (CardValue cardValue : CardValue.values()) {
      for (CardSymbol cardSymbol : CardSymbol.values()) {
        NormalCard normalCard = new NormalCard(cardValue, cardSymbol);
        assertTrue(foundNormalCard.contains(normalCard));
      }
    }
  }
}
