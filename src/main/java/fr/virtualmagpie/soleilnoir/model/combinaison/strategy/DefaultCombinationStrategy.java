package fr.virtualmagpie.soleilnoir.model.combinaison.strategy;

import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.card.CardValue;
import fr.virtualmagpie.soleilnoir.model.card.JokerCard;
import fr.virtualmagpie.soleilnoir.model.card.NormalCard;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Given a set of cards, find the best combination possible. Choice of the best combination is based
 * on order implemented in Combination class (ie the higher value among the higher quantity). We
 * must also take into account Joker, which can become any card.
 *
 * <p>No further rule is applied in this basic strategy.
 *
 * <p>For instance:
 *
 * <ul>
 *   <li>With a hand of 1 Ace and 2 Five, the best combination is 2[5]
 *   <li>With a hand of 1 Two, 1 Queen and 1 Joker, the best combination is 2[Q]
 *   <li>With a hand of 2 Jokers, the best combination is 2[A]
 * </ul>
 */
public class DefaultCombinationStrategy implements CombinationStrategy {

  public Combination findBestCombination(Card[] cards) {
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

    // Take jokers into account in possible combination
    if (nbJoker > 0) {
      for (Combination combination : possibleCombination.values()) {
        combination.setNbCard(combination.getNbCard() + nbJoker);
      }
    }

    // Pick best combination
    return possibleCombination.values().stream().max(Comparator.naturalOrder()).orElseThrow();
  }
}
