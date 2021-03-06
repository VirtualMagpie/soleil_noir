package fr.virtualmagpie.soleilnoir.model.card;

/**
 * This class create 54 cards for a classic playing card deck. Generated objects are saved, to
 * prevent more call to constructor when requesting an already existing card.
 */
public class CardProvider {
  private static final Card[] cards;

  static {
    cards = new Card[54];
    int i = 0;
    for (CardSymbol cardSymbol : CardSymbol.values()) {
      for (CardValue cardValue : CardValue.values()) {
        cards[i++] = new NormalCard(cardValue, cardSymbol);
      }
    }
    cards[i++] = new JokerCard();
    cards[i++] = new JokerCard();
  }

  public static Card get(int i) {
    return cards[i];
  }

  public static int length() {
    return cards.length;
  }
}
