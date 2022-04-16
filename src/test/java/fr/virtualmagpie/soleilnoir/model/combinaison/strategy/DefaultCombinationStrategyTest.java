package fr.virtualmagpie.soleilnoir.model.combinaison.strategy;

import fr.virtualmagpie.soleilnoir.model.card.*;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultCombinationStrategyTest {

  @ParameterizedTest(name = "{index} => {0}")
  @MethodSource("argumentProvider")
  void expectedResult(String name, Card[] cards, Combination expected) {
    DefaultCombinationStrategy combinationStrategy = new DefaultCombinationStrategy();
    assertEquals(expected, combinationStrategy.findBestCombination(cards));
  }

  private static Stream<Arguments> argumentProvider() {
    Card[] hand1 = {
      new NormalCard(CardValue.FIVE, CardSymbol.CLUB),
      new NormalCard(CardValue.ACE, CardSymbol.CLUB),
      new NormalCard(CardValue.FIVE, CardSymbol.HEART)
    };
    Combination expected1 = new Combination(2, CardValue.FIVE);

    Card[] hand2 = {
      new NormalCard(CardValue.TWO, CardSymbol.CLUB),
      new NormalCard(CardValue.QUEEN, CardSymbol.CLUB),
      new JokerCard()
    };
    Combination expected2 = new Combination(2, CardValue.QUEEN);

    Card[] hand3 = {new JokerCard(), new JokerCard()};
    Combination expected3 = new Combination(2, CardValue.ACE);

    return Stream.of(
        Arguments.of("Normal strategy - no joker", hand1, expected1),
        Arguments.of("Normal strategy - a joker", hand2, expected2),
        Arguments.of("Normal strategy - only jokers", hand3, expected3));
  }
}
