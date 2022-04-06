package fr.virtualmagpie.soleilnoir.model.combinaison;

import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.card.CardValue;
import fr.virtualmagpie.soleilnoir.model.card.JokerCard;
import fr.virtualmagpie.soleilnoir.model.card.NormalCard;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CombinationStrategy {

  public static Combination findBestCombination(Card[] cards) {
    if (cards.length == 0) {
      throw new IllegalArgumentException("No combination possible in empty hand");
    }

    int nbJoker = 0;
    Map<CardValue, Combination> possibleCombination = new HashMap<>();

    // See all cards for possible combination, count jokers for later
    for (Card card : cards) {
      if (card instanceof JokerCard) {
        nbJoker++;
      } else if (card instanceof NormalCard) {
        NormalCard normalCard = (NormalCard) card;
        if (!possibleCombination.containsKey(normalCard.getValue())) {
          Combination combination = new Combination(1, normalCard.getValue());
          possibleCombination.put(normalCard.getValue(), combination);
        } else {
          Combination combination = possibleCombination.get(normalCard.getValue());
          combination.setNbCard(combination.getNbCard() + 1);
        }
      }
    }

    // In case hand contains only jokers, consider an Ace combination
    if (possibleCombination.isEmpty()) {
      Combination combination = new Combination(0, CardValue.ACE);
      possibleCombination.put(CardValue.ACE, combination);
    }

    // Take joker into account in possible combination
    if (nbJoker > 0) {
      for (Combination combination : possibleCombination.values()) {
        combination.setNbCard(combination.getNbCard() + nbJoker);
      }
    }

    // Pick best combination
    return possibleCombination.values().stream().max(Comparator.naturalOrder()).orElseThrow();
  }
}
