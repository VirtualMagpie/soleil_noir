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

class AdvantageLimitedCombinationStrategyTest {

  @ParameterizedTest(name = "{index} => {0}")
  @MethodSource("argumentProvider")
  void expectedResult(
      String name,
      Card[] cards,
      Combination expectedForVariationBefore,
      Combination expectedForVariationAfter) {
    AdvantageLimitedCombinationStrategy combinationStrategyVariationBefore =
        new AdvantageLimitedCombinationStrategy(CardSymbol.CLUB, true);
    AdvantageLimitedCombinationStrategy combinationStrategyVariationAfter =
        new AdvantageLimitedCombinationStrategy(CardSymbol.CLUB, false);
    assertEquals(
        expectedForVariationBefore, combinationStrategyVariationBefore.findBestCombination(cards));
    assertEquals(
        expectedForVariationAfter, combinationStrategyVariationAfter.findBestCombination(cards));
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

    Card[] hand4 = {
      new NormalCard(KING, SPADE), new NormalCard(JACK, CLUB), new NormalCard(FIVE, CLUB)
    };
    Combination expected4 = new Combination(2, JACK);

    Card[] hand5 = {new NormalCard(TEN, SPADE), new NormalCard(JACK, CLUB), new JokerCard()};
    Combination expectedBefore5 = new Combination(2, ACE);
    Combination expectedAfter5 = new Combination(2, JACK);

    Card[] hand6 = {new NormalCard(KING, SPADE), new NormalCard(JACK, CLUB), new JokerCard()};
    Combination expectedBefore6 = new Combination(2, ACE);
    Combination expectedAfter6 = new Combination(2, KING);

    Card[] hand7 = {
      new NormalCard(NINE, SPADE),
      new NormalCard(NINE, DIAMOND),
      new NormalCard(JACK, CLUB),
      new JokerCard()
    };
    Combination expected7 = new Combination(3, NINE);

    return Stream.of(
        Arguments.of("Normal strategy - no joker", hand1, expected1, expected1),
        Arguments.of("Normal strategy - a joker", hand2, expected2, expected2),
        Arguments.of("Normal strategy - only jokers", hand3, expected3, expected3),
        Arguments.of("Advantage strategy - no joker", hand4, expected4, expected4),
        Arguments.of(
            "Advantage strategy - a joker (advantage card stronger than normal card)",
            hand5,
            expectedBefore5,
            expectedAfter5),
        Arguments.of(
            "Advantage strategy - a joker (normal card stronger than advantage card)",
            hand6,
            expectedBefore6,
            expectedAfter6),
        Arguments.of(
            "Advantage strategy - a joker (not becoming advantage)", hand7, expected7, expected7));
  }
}
