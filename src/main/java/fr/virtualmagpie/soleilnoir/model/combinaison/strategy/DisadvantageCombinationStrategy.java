package fr.virtualmagpie.soleilnoir.model.combinaison.strategy;

import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.card.CardSymbol;
import fr.virtualmagpie.soleilnoir.model.card.CardValue;
import fr.virtualmagpie.soleilnoir.model.card.NormalCard;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Given a set of cards, find the best combination possible. Choice of the best combination is based
 * on order implemented in Combination class (ie the higher value among the higher quantity). We
 * must also take into account Joker, which can become any card.
 *
 * <p>In this strategy, disadvantage rule is used. Drawing with disadvantage means that every card
 * with a specific symbol (excepted ACE card) is not applied in final combination. Otherwise,
 * default combination strategy is applied.
 *
 * <p>For instance (with disadvantage on CLUB):
 *
 * <ul>
 *   <li>With a hand of 1 Queen (not club) and 1 King (club), the best combination is 1[Q]
 *   <li>With a hand of 1 Queen (not club) and 1 Ace (club), the best combination is 1[A]
 *   <li>With a hand of 1 Ten (club) and 1 Jack (club), no card remains for final combination (in
 *       that case we return a fictive combination of 0[A])
 * </ul>
 */
@RequiredArgsConstructor
public class DisadvantageCombinationStrategy implements CombinationStrategy {

  private final CardSymbol cardSymbol;
  private final DefaultCombinationStrategy defaultCombinationStrategy =
      new DefaultCombinationStrategy();

  @Override
  public Combination findBestCombination(Card[] cards) {
    if (cards.length == 0) {
      throw new IllegalArgumentException("No combination possible in empty hand");
    }

    Card[] filteredCards = Arrays.stream(cards).filter(this::keepCard).toArray(Card[]::new);
    if (filteredCards.length > 0) {
      return defaultCombinationStrategy.findBestCombination(filteredCards);
    } else {
      return new Combination(0, CardValue.ACE);
    }
  }

  private boolean keepCard(Card card) {
    if (!(card instanceof NormalCard)) {
      return true;
    }
    NormalCard normalCard = (NormalCard) card;
    return normalCard.getValue().equals(CardValue.ACE)
        || !normalCard.getSymbol().equals(cardSymbol);
  }
}
