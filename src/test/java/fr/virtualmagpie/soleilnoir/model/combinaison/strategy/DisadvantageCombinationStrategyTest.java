package fr.virtualmagpie.soleilnoir.model.combinaison.strategy;

import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.card.CardSymbol;
import fr.virtualmagpie.soleilnoir.model.card.JokerCard;
import fr.virtualmagpie.soleilnoir.model.card.NormalCard;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static fr.virtualmagpie.soleilnoir.model.card.CardSymbol.*;
import static fr.virtualmagpie.soleilnoir.model.card.CardValue.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DisadvantageCombinationStrategyTest {

  @ParameterizedTest(name = "{index} => {0}")
  @MethodSource("argumentProvider")
  void expectedResult(String name, Card[] cards, Combination expected) {
    DisadvantageCombinationStrategy combinationStrategy =
        new DisadvantageCombinationStrategy(CardSymbol.CLUB);
    assertEquals(expected, combinationStrategy.findBestCombination(cards));
  }

  private static Stream<Arguments> argumentProvider() {
    Card[] hand1 = {
      new NormalCard(FIVE, DIAMOND), new NormalCard(ACE, DIAMOND), new NormalCard(FIVE, HEART)
    };
    Combination expected1 = new Combination(2, FIVE);

    Card[] hand2 = {new NormalCard(TWO, DIAMOND), new NormalCard(QUEEN, DIAMOND), new JokerCard()};
    Combination expected2 = new Combination(2, QUEEN);

    Card[] hand3 = {new JokerCard(), new JokerCard()};
    Combination expected3 = new Combination(2, ACE);

    Card[] hand4 = {new NormalCard(QUEEN, SPADE), new NormalCard(KING, CLUB)};
    Combination expected4 = new Combination(1, QUEEN);

    Card[] hand5 = {new NormalCard(QUEEN, SPADE), new NormalCard(ACE, CLUB)};
    Combination expected5 = new Combination(1, ACE);

    Card[] hand6 = {new NormalCard(TEN, CLUB), new NormalCard(JACK, CLUB)};
    Combination expected6 = new Combination(0, ACE);

    return Stream.of(
        Arguments.of("Normal strategy - no joker", hand1, expected1),
        Arguments.of("Normal strategy - a joker", hand2, expected2),
        Arguments.of("Normal strategy - only jokers", hand3, expected3),
        Arguments.of("Disadvantage strategy - drop a card", hand4, expected4),
        Arguments.of("Disadvantage strategy - does not drop ace", hand5, expected5),
        Arguments.of("Disadvantage strategy - all card are dropped", hand6, expected6));
  }
}
