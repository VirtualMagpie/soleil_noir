package fr.virtualmagpie.soleilnoir.model.combinaison.strategy;

import fr.virtualmagpie.soleilnoir.model.card.*;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;

import java.util.*;

/**
 * Given a set of cards, find the best combination possible. Choice of the best combination is based
 * on order implemented in Combination class (ie the higher value among the higher quantity). We
 * must also take into account Joker, which can become any card.
 *
 * <p>In this strategy, advantage rule is used (we arbitrarily use CLUB as the card symbol for this
 * advantage). Drawing with advantage means that every card with this symbol is considered to be
 * equal to card with the higher symbol. Otherwise, the default combination strategy is used.
 *
 * <p>In this strategy, we suppose Joker value is chosen after all cards are dealt. Furthermore,
 * advantaged cards change their values after Joker has changed its value (meaning we can optimize
 * result when choosing a specific value for Joker).
 *
 * <p>For instance:
 *
 * <ul>
 *   <li>With a hand of 1 King (not club), 1 Jack (club) and 1 Five (club), the best combination is
 *       2[J]
 *   <li>With a hand of 1 Ten (not club), 1 Jack (club) and 1 Joker, the best combination is 2[A]
 *       (Joker change its value to Ace of club)
 *   <li>With a hand of 1 King (not club), 1 Jack (club) and 1 Joker, the best combination is 3[K]
 *       (Joker change its value to King of club)
 *   <li>With a hand of 2 Nine (not club), 1 Jack (club) and 1 Joker, the best combination is 3[9]
 *       (instead of changing Joker into club value, it is better here to just change it to a
 *       non-club Nine)
 * </ul>
 *
 * <p>Personal note: I personally don't like this capacity of optimizing result when choosing Joker
 * value (I think this is overpowered). I therefore implement this strategy to verify how powerful
 * this strategy is. For my personal use when I am Game Master, I consider a variation of this
 * strategy, which will be implemented in another class.
 */
public class AdvantageCombinationStrategy implements CombinationStrategy {

  @Override
  public Combination findBestCombination(Card[] cards) {
    if (cards.length == 0) {
      throw new IllegalArgumentException("No combination possible in empty hand");
    }

    int nbJoker = 0;
    List<CardValue> advantageCardValues = new ArrayList<>();
    Map<CardValue, Combination> possibleCombinationNormalCard = new HashMap<>();

    // See all cards for possible combination, count jokers for later, keep advantage cards for
    // later
    for (Card card : cards) {
      if (card instanceof JokerCard) {
        // Keep joker for later
        nbJoker++;
      } else if (card instanceof NormalCard) {
        NormalCard normalCard = (NormalCard) card;
        // Keep advantage cards for later
        if (normalCard.getSymbol().equals(CardSymbol.CLUB)) {
          advantageCardValues.add(normalCard.getValue());
        }
        // Combination with other cards
        else if (!possibleCombinationNormalCard.containsKey(normalCard.getValue())) {
          Combination combination = new Combination(1, normalCard.getValue());
          possibleCombinationNormalCard.put(normalCard.getValue(), combination);
        } else {
          Combination combination = possibleCombinationNormalCard.get(normalCard.getValue());
          combination.setNbCard(combination.getNbCard() + 1);
        }
      }
    }

    // Combination from advantage cards (without joker, so this combination can be upgraded to an
    // higher value with jokers)
    Optional<Combination> combinationAdvantageCards =
        findCombinationFromAdvantageCardOnly(advantageCardValues);

    // Take jokers and advantage cards into account in possible combination
    List<Combination> possibleCombination =
        findPossibleCombination(possibleCombinationNormalCard, combinationAdvantageCards, nbJoker);

    // Pick best combination
    return possibleCombination.stream().max(Comparator.naturalOrder()).orElseThrow();
  }

  /**
   * Define combination taken by advantage cards (without using joker). All cards take the value of
   * the higher card.
   */
  private Optional<Combination> findCombinationFromAdvantageCardOnly(
      List<CardValue> advantageCardValues) {

    if (advantageCardValues.isEmpty()) {
      return Optional.empty();
    } else {
      CardValue higherValue =
          advantageCardValues.stream().max(Comparator.naturalOrder()).orElseThrow();
      Combination combination = new Combination(advantageCardValues.size(), higherValue);
      return Optional.of(combination);
    }
  }

  /**
   * Given a card value, how many cards among advantage cards and jokers can be used to take this
   * card value?
   * <li>jokers can always take this card value
   * <li>advantage card combination can take this card value if this is its current value
   * <li>when both joker and advantage card are present, joker can be used to upgrade advantage card
   *     (but not downgrade)
   */
  private int getNbSpecialCardsUsableForValue(
      CardValue cardValue, Optional<Combination> combinationAdvantageCards, int nbJoker) {

    if (combinationAdvantageCards.isEmpty()) {
      return nbJoker;
    }

    CardValue advantageCardHigherValue = combinationAdvantageCards.get().getCardValue();
    int nbAdvantageCard = combinationAdvantageCards.get().getNbCard();
    if (cardValue.equals(advantageCardHigherValue)) {
      return nbJoker + nbAdvantageCard;
    } else if (nbJoker > 0 && cardValue.compareTo(advantageCardHigherValue) > 0) {
      return nbJoker + nbAdvantageCard;
    } else {
      return nbJoker;
    }
  }

  private List<Combination> findPossibleCombination(
      Map<CardValue, Combination> possibleCombinationNormalCard,
      Optional<Combination> combinationAdvantageCards,
      int nbJokers) {
    List<Combination> possibleCombination = new ArrayList<>();
    for (CardValue cardValue : CardValue.values()) {
      int nbNormalCards =
          possibleCombinationNormalCard.containsKey(cardValue)
              ? possibleCombinationNormalCard.get(cardValue).getNbCard()
              : 0;
      int nbSpecialCards =
          getNbSpecialCardsUsableForValue(cardValue, combinationAdvantageCards, nbJokers);
      int nbCards = nbNormalCards + nbSpecialCards;
      if (nbCards > 0) {
        possibleCombination.add(new Combination(nbCards, cardValue));
      }
    }
    return possibleCombination;
  }
}
