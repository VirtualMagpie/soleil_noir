package fr.virtualmagpie.soleilnoir.model.combinaison.strategy;

import fr.virtualmagpie.soleilnoir.model.card.*;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Given a set of cards, find the best combination possible. Choice of the best combination is based
 * on order implemented in Combination class (ie the higher value among the higher quantity). We
 * must also take into account Joker, which can become any card.
 *
 * <p>In this strategy, advantage rule is used. Drawing with advantage means that every card with a
 * specific symbol is considered to be equal to card with the higher value (among those with this
 * same symbol). Otherwise, the default combination strategy is used.
 *
 * <p>In this strategy, I use a personal interpretation of advantage rule. This interpretation
 * intends to make this rule less powerful. In this interpretation, the combination from advantage
 * cards is distinct from normal card and cannot be merged together. However, the player can choose
 * not to apply advantage card rule when this produce a lower result. With this interpretation, when
 * cards are drawn (with some advantages cards, and some non-advantage cards), the drawer can
 * either: use all cards normally with default strategy OR use ONLY advantage cards (and jokers) and
 * consider them as having all the same value.
 *
 * <p>Variation: based on attribute 'canJokerUpgradeAdvantageCard', we can define 2 variations:
 *
 * <ul>
 *   <li>True --> Advantage cards change their value AFTER Joker has defined its value. Joker can
 *       therefore take an ACE value, converting all advantage cards into ACE.
 *   <li>false --> Advantage cards change their value BEFORE Joker has defined its value. Value
 *       taken by Joker will not change value of advantage card. It may no longer be interesting to
 *       convert Joker into ACE, but rather convert Joker into the max value of the combination.
 * </ul>
 *
 * <p>For instance:
 *
 * <ul>
 *   <li>With a hand of 1 King (not club), 1 Jack (club) and 1 Five (club), the best combination is
 *       2[J]
 *   <li>With a hand of 1 Ten (not club), 1 Jack (club) and 1 Joker, the best combination is 2[A]
 *       (for variation TRUE) (Joker change its value to Ace of club) // 2[J] (for variation FALSE)
 *   <li>With a hand of 1 King (not club), 1 Jack (club) and 1 Joker, the best combination is 2[A]
 *       (for variation TRUE) (Joker change its value to Ace of club -- Here the strategy is
 *       different from the normal advantage strategy, we cannot have both the King and the Jack in
 *       the same combination, we must instead either choose between a pair of King (using King card
 *       and Joker), or a pair of Club (the Jack and the Joker)) // 2[K] (for variation FALSE)
 *   <li>With a hand of 2 Nine (not club), 1 Jack (club) and 1 Joker, the best combination is 3[9]
 *       (instead of changing Joker into club value, it is better here to just change it to a
 *       non-club Nine)
 * </ul>
 *
 * <p>Personal note: This is a custom interpretation of advantage strategy I use when I am Game
 * Master. It is implemented here so I can compare statistics from this strategy, against statistics
 * of the real strategy.
 */
@RequiredArgsConstructor
public class AdvantageLimitedCombinationStrategy implements CombinationStrategy {

  private final CardSymbol cardSymbol;
  private final boolean canJokerUpgradeAdvantageCard;

  public Combination findBestCombination(Card[] cards) {
    if (cards.length == 0) {
      throw new IllegalArgumentException("No combination possible in empty hand");
    }

    int nbJoker = 0;
    Map<CardValue, Combination> possibleCombination = new HashMap<>();
    Combination advantageCombination = new Combination(0, CardValue.TWO);

    // See all cards for possible combination, count jokers for later
    for (Card card : cards) {
      if (card instanceof JokerCard) {
        nbJoker++;
      } else if (card instanceof NormalCard) {
        NormalCard normalCard = (NormalCard) card;
        // Add card to possible combination for default strategy
        if (!possibleCombination.containsKey(normalCard.getValue())) {
          Combination combination = new Combination(1, normalCard.getValue());
          possibleCombination.put(normalCard.getValue(), combination);
        } else {
          Combination combination = possibleCombination.get(normalCard.getValue());
          combination.setNbCard(combination.getNbCard() + 1);
        }
        // Also add card in advantage combination when advantage card
        if (normalCard.getSymbol().equals(cardSymbol)) {
          advantageCombination.setNbCard(advantageCombination.getNbCard() + 1);
          if (normalCard.getValue().compareTo(advantageCombination.getCardValue()) > 0) {
            advantageCombination.setCardValue(normalCard.getValue());
          }
        }
      }
    }

    // In case hand contains jokers, consider advantage combination applying jokers
    // (for joker-only hands, this will be a combination of ACE)
    if (nbJoker > 0) {
      if (canJokerUpgradeAdvantageCard || advantageCombination.getNbCard() == 0) {
        advantageCombination.setCardValue(CardValue.ACE);
      }
      advantageCombination.setNbCard(advantageCombination.getNbCard() + nbJoker);
    }

    // Take jokers into account in possible normal combination
    if (nbJoker > 0) {
      for (Combination combination : possibleCombination.values()) {
        combination.setNbCard(combination.getNbCard() + nbJoker);
      }
    }

    // Put advantage combination in list of possible combination (unless default combination is
    // better)
    if (!possibleCombination.containsKey(advantageCombination.getCardValue())
        || possibleCombination.get(advantageCombination.getCardValue()).getNbCard()
            < advantageCombination.getNbCard()) {
      possibleCombination.put(advantageCombination.getCardValue(), advantageCombination);
    }

    // Pick best combination
    return possibleCombination.values().stream().max(Comparator.naturalOrder()).orElseThrow();
  }
}
